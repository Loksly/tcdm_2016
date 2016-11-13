#!/usr/bin/env python
# coding: utf-8

from __future__ import with_statement, print_function
import json
from subprocess import call, Popen, PIPE
from collections import OrderedDict

privateip_start = 6
for n in range(1,5):
	# Crea IP pública
	sn = str(n)
	command = "azure network public-ip create HadoopGroup DataNode"+sn+"IP --allocation-method Static -l westeurope"
	call(command, shell=True)
	# Crea el NIC
	command = "azure network nic create HadoopGroup DataNode"+sn+"NIC -k HadoopSubnet -m HadoopVnet -p DataNode"+str(sn)+"IP -a 10.0.0."+str(privateip_start)+" -l westeurope"
	call(command, shell=True)
	privateip_start = privateip_start + 1
	# Obtiene la info del NIC
	command = "azure network nic show HadoopGroup DataNode"+sn+"NIC"
	outinfo = Popen(command, shell=True, stdout=PIPE)
	nicinfo = outinfo.stdout.readlines()
	nicid = None
	for line in nicinfo:
		sline = str(line)
		if "Id" in sline:
			nicid = sline.strip().split(':')[2][1:-3]
			break
	# Modifica la template
	filename = "datanode"+sn+"-template.json"
	with open(filename, "r+") as jsonfile:
		jsondata = json.load(jsonfile, object_pairs_hook=OrderedDict)

	jsondata["parameters"]['networkInterfaceId']['defaultValue'] = nicid+"NIC"
	with open(filename, "w+") as jsonfile:
		jsonfile.write(json.dumps(jsondata))
	
	# Realiza el despliegue
	command = "azure group deployment create HadoopGroup DataNode"+sn+" -f ./"+filename
	call(command, shell=True)