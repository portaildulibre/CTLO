#!/bin/bash
# Correcteur terminologique - éradication des anglicismes.
# Copyright (C) 2006 Linagora SA - Manuel Odesser modesser@linagora.com
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
#

sedscript="correction_xml.sed"

function usage() {
	echo "Usage: $0 in_file out_file"
	exit
}
if [ -z $1 ]; then
	usage
fi
if [ -z $2 ]; then
	usage
fi
xml=$1
out=$2
cat $xml | sed -f $sedscript > $out
