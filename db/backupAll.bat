mysqldump -h127.0.0.1 -P3306 -uroot -p --single-transaction -F --skip-extended-insert --compact -R -r min_blog.sql -B min_blog
