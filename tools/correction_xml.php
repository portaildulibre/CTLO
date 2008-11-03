<?php
/*
Correcteur terminologique - éradication des anglicismes.
Copyright (C) 2006 Linagora SA
	Manuel Odesser <modesser@linagora.com>
	Sebastien Bahloul <bahloul@linagora.com>

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

/**
 * convertisseur base CRITER -> thesaurus OOo
 * à utiliser en ligne de commande.
 */

class Anglicisme {
	var $data;
	var $anglicisme;

	function add($domaine,$synonymes) {
		if (is_array($domaine)) {
			foreach($domaine as $d) {
				$this->add($d,$synonymes);
			}
		} else {
			if (isset($this->data[$domaine])) {
				$this->data[$domaine] = array_merge($this->data[$domaine],$synonymes);
			} else {
				$this->data[$domaine]=$synonymes;
			}
		}
	}
	function getData($domaine = null) {
		if (is_null($domaine)) {
			return $this->data;
		} elseif (isset($this->data[$domaine])) {
			return $this->data[$domaine];
		} else {
			return null;
		}
	}
	function setAnglicisme($anglicisme) {
		$this->anglicisme = $anglicisme;
	}
	function getAnglicisme() {
		return $this->anglicisme;
	}
}

function usage() {
	$bin = $_SERVER['argv'][0];
	echo "Usage: $bin in_file";
}

function ttrim($s) {
	return trim($s);
}

function addToThesaurus($anglicisme) {
	global $thesaurus;
	if (!isset($thesaurus[$anglicisme->getAnglicisme()])) {
		// il n'y a pas encore d'anglicisme enregistré
		$thesaurus[$anglicisme->getAnglicisme()] = $anglicisme;
	} else {
if ($anglicisme->getAnglicisme() == 'alkylation') { // trim attenuation
}
		// on a déjà un anglicisme enregistré
		$domaines = array_keys($anglicisme->getData());
		$ang_existant = $thesaurus[$anglicisme->getAnglicisme()];
		foreach ($domaines as $domaine) {
			$ang_existant->add($domaine,$anglicisme->getData($domaine));
			$thesaurus[$anglicisme->getAnglicisme()] = $ang_existant;
		}
	}
}

function parseDB($filename) {
	global $encoding;
	$data = implode('',file($filename));
	$articles = DOMDocument::loadXML($data);
	$enc = $articles->xmlEncoding;
	if (!is_null($enc) && !empty($enc)) {
		$encoding = $enc;
	}
	foreach ($articles->getElementsByTagName('Article') as $article) {
		$termes = array();
		$domaines = array();
		$equivalents = array();
		$anglicismes = array();

		$default = array();
		$synonyme = array();
		$privilegie = array();
		$abreviation = array();
		foreach($article->childNodes as $node) {
			switch($node->nodeName) {
				case 'Aeviter':
				case 'Ainterroger':
					// ce sont les mêmes info ; le tag a changé en cours de route.
					break;
				case 'DatePub':
					break;
				case 'Definition':
					break;
				case 'Domaines':
					foreach($node->childNodes as $subnode) {
						$domaine = ttrim($subnode->nodeValue);
						if (!empty($domaine)) {
							$domaines[] = $domaine;
						}
					}
					break;
				case 'Equivalents':
					$tmp = array();
					foreach($node->childNodes as $equiv_etranger) {
						// on boucle sur les <Equivalent-etranger>
						if (is_a($equiv_etranger,'DOMText')) {
							continue;
						}
						foreach($equiv_etranger->childNodes as $subnode) {
							if ($subnode->nodeName == 'Variantes') {
								// on prend les variantes
								foreach($subnode->childNodes as $subsubnode) {
									$tmp[] = ttrim($subsubnode->nodeValue);
								}
							} else {
								if (is_a($subnode,'DOMText')) {
									$tmp[] = ttrim($subnode->nodeValue);
								}
							}
						}
					}
					foreach($tmp as $equivalent) {
						if (!empty($equivalent)) {
							$equivalents[] = $equivalent;
							if (strtolower($equivalent) != $equivalent) {
								// on ajoute une version en minuscules, pour
								// détection plus aisée à l'utilisation.
								$equivalents[] = strtolower($equivalent);
							}
						}
					}
					break;
				case 'Note':
					break;
				case 'Termes':
					foreach($node->childNodes as $subnode) {
						if (is_a($subnode,'DOMText')) {
							$statut = '';
						} else {
							$statut = $subnode->getAttribute('Statut');
						}
						$terme = ttrim($subnode->nodeValue);
						if (!empty($terme)) {
							switch($statut) {
								case 'privilegie':
									$privilegie[] = $terme;
									break;
								case 'synonyme':
									$synonyme[] = $terme;
									break;
								case 'abreviation':
									$abreviation[] = $terme;
									break;
								default:
									$default[] = $terme;
									break;
							}
						}
					}
					break;
				case 'Voir-aussi':
					break;
				default:
					// ouch
			}
		}
		// Equivalents
		if (empty($equivalents)) {
			// pas d'équivalent anglais
			continue;
		}
		$tmp = array();
		foreach($equivalents as $equivalent) {
			// on supprime les parenthèses et leur contenu. ex :
			// 'food and agriculture (industry)' ==> 'food and agriculture'
			// (sinon, impossible de trouver ces locutions dans le texte à vérifier)
			$tmp[] = ttrim(preg_replace("!\(.*\)!",'',$equivalent));
		}
		$equivalents = array_unique($tmp);
		sort($equivalents,SORT_STRING);

		// Termes (FR)
		//   dans l'ordre :
		//   "entrée principale, forme abrégée, abréviation, synonyme"
		$default = array_unique($default);
		$synonyme = array_unique($synonyme);
		$privilegie = array_unique($privilegie);
		$abreviation = array_unique($abreviation);
		$termes = array_merge($privilegie,$abreviation,$synonyme,$default);
		$termes = array_unique($termes);
		//sort($termes,SORT_STRING);

		// Domaines
		$domaines = array_unique($domaines);
		if (!count($domaines)) {
			$domaines[] = 'non précisé';
		}
		sort($domaines,SORT_STRING);

		// ajout dans la base
		foreach ($equivalents as $equivalent) {
			$anglicisme = new Anglicisme;
			$anglicisme->setAnglicisme($equivalent);
			$anglicisme->add($domaines,$termes);
			$anglicismes[] = $anglicisme;
		}
		foreach ($anglicismes as $anglicisme) {
			addToThesaurus($anglicisme);
		}
	}
}
function exportDB() {
	global $encoding;
	global $thesaurus;
	$out = "$encoding\n";
	foreach ($thesaurus as $entry) {
		$out .= $entry->getAnglicisme();
		$out .= '|';
		$out .= count($entry->getData());
		$out .= "\n";
		foreach ($entry->getData() as $domaine=>$synonymes) {
			$out .= $domaine;
			$out .= '|';
			$out .= implode('|',$synonymes);
			$out .= "\n";
		}
	}
	return $out;
}
function printr($x) {
	print_r($x);
	echo "\n";
}

$thesaurus = array();
$anglicisme = null;
$encoding = 'ISO-8859-1';

if ($_SERVER['argc'] < 2) {
	usage();
	die();
}
$xml = $_SERVER['argv'][1];

parseDB($xml);
//$out = iconv($encoding,'utf8',exportDB());
//echo $out;
echo exportDB();

?>
