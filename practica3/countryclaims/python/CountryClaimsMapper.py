#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
for line in sys.stdin:
    fields = line.strip().split(",")
    if fields[8] == "":
    	fields[8] = "0"
    print "%s\t%s" % (fields[4].replace('"', '').encode('utf-8'), fields[8].encode('utf-8'))
