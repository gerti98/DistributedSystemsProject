echo "my_supervisor:start_link()." | nohup erl -name 'server@172.18.0.7' -setcookie "abcde" &