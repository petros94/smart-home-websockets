#!/bin/bash

while :
do
	echo -n `date "+%Y-%m-%d %T"`", " >> $1
	docker stats --format "{{.Name}}, {{.CPUPerc}}, {{.MemUsage}}, {{.MemPerc}}, {{.NetIO}}, {{.BlockIO}}" --no-stream | grep websockets-activemq_device-management-ms_1 >> $1
	sleep $2
done

Thu, Apr 16, 2020  6:24:24 PM
997e72c7abfd        websockets-activemq_device-management-ms_1   1.20%               205.7MiB / 3.846GiB   5.22%               2.21MB / 1.36MB     12.5MB / 81.9kB     60
Thu, Apr 16, 2020  6:24:26 PM
997e72c7abfd        websockets-activemq_device-management-ms_1   0.22%               205.7MiB / 3.846GiB   5.22%               2.21MB / 1.36MB     12.5MB / 81.9kB     60
Thu, Apr 16, 2020  6:24:28 PM
Thu, Apr 16, 2020  6:24:30 PM
Thu, Apr 16, 2020  6:24:31 PM
Thu, Apr 16, 2020  6:24:32 PM
Thu, Apr 16, 2020  6:27:26 PM
997e72c7abfd        websockets-activemq_device-management-ms_1   0.33%               210MiB / 3.846GiB     5.33%               2.35MB / 1.47MB     12.5MB / 81.9kB     60
Thu, Apr 16, 2020  6:27:29 PM
997e72c7abfd        websockets-activemq_device-management-ms_1   0.17%               210MiB / 3.846GiB     5.33%               2.36MB / 1.47MB     12.5MB / 81.9kB     60
Thu, Apr 16, 2020  6:27:31 PM
Thu, Apr 16, 2020  6:27:33 PM
Thu, Apr 16, 2020  6:27:35 PM
