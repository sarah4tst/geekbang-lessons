# geekbang-lessons
极客时间课程工程
Jolokia 读写属性
```shell script
curl http://localhost:8080/jolokia/read/SERVLET:name=RegisterController
curl http://localhost:8080/jolokia/write/SERVLET:name=RegisterController/customResponseHeaderVal/2.0
```
Jolokia 写入属性后的功能校验
```shell script
# before change
curl -sI http://localhost:8080/register
# result 包括: X-CUSTOM-TAG: 1.0
# perform change
curl -X POST 'http://localhost:8080/jolokia' --header 'Content-Type: application/json' --data-raw '{
    "type": "write",
    "mbean": "SERVLET:name=RegisterController",
    "attribute": "customResponseHeaderVal",
    "value": "3.0"
}'
# after change
curl -sI http://localhost:8080/register
# result 包括: X-CUSTOM-TAG: 3.0
```

