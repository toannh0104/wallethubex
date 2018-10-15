# Run
    mvn clean install
    
    java -jar target\parser.jar --accesslog=path\to\file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
    
# MySQL schema
    
    CREATE TABLE `access_log` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `date` datetime NOT NULL,
      `ip_address` varchar(255) NOT NULL,
      PRIMARY KEY (`id`)
    )
    
    CREATE TABLE `blocked_ip` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `comments` varchar(255) NOT NULL,
      `ip` varchar(255) NOT NULL,
      PRIMARY KEY (`id`)
    )
#Queries
    
    SELECT ip_address, count(id) AS th
    FROM access_log
    WHERE DATE BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.13:59:00'
    GROUP BY ip_address
    HAVING th > 100