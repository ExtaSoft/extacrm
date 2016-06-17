# Настройки сервера
* key_buffer_size = 1024M
* max_allowed_packet = 1024M
* sort_buffer_size = 64K
* myisam_sort_buffer_size = 1365M
* read_buffer_size = 256K
* read_rnd_buffer_size = 256K
* innodb_buffer_pool_size = 2048M

# Cron

0 1 * * * /var/lib/jelastic/bin/backup_script.sh -m dump -u root -p SECRET -d extacrm

