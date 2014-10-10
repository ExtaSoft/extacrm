#!/bin/bash

HOST='localhost'
BACKUPDIR='/var/lib/jelastic/backup'
LOG_FILE="/var/log/mysql/logdb_bckp.log";
SOCKET='/var/lib/mysql/mysql.sock';
EXCLUDE=('information_schema');
# 1 - verbose 0 - quit, only  error
__VERBOSE=1

MYSQL=`which mysql`;
MDUMP=`which mysqldump`;
MKDIR=`which mkdir`;
BZIP2=`which bzip2`;
SED=`which sed`;
WC=`which wc`;
#---------------------------
OPTS="--quote-names --opt --compress --single-transaction"
DATE=`date +%Y-%m-%d_%Hh%Mm`
SECONDS=`date +%s`


TMP="/tmp/backup_${SECONDS}.tmp"

function log() {
    if [ $__VERBOSE -gt 0 ]; then
        echo -n `date +%D.%k:%M:%S.%N` >> ${LOG_FILE}
        echo ": $@" >> ${LOG_FILE}
    fi
    if [ $__VERBOSE -gt 1 ]; then
        echo -n `date +%D.%k:%M:%S.%N`
        echo ": $@"
    fi
}

function db_dump () {
    local db_name=$1
    local file_name=$2
    ${MDUMP} --user=${DBUSER} --password=${DBPASSWORD} --host=${HOST} ${OPTS} --database ${db_name} 2>${TMP} > ${file_name}
    last_eroror=$?
    if [ $last_eroror -eq 0 ]
    then 
       $BZIP2 ${file_name};
    else 
       rm -rf ${file_name};
    fi
    return $last_eroror;
}

function dumpall () {
    local file_name="${BACKUPDIR}/all-db-${DATE}.sql";
    log "user: $DBUSER ; password: $DBPASSWORD ;";
    create_directories;
    ${MDUMP} --user=${DBUSER} --password=${DBPASSWORD} --host=${HOST} ${OPTS} --all-databases 2>${TMP} > ${file_name}
    last_eroror=$?;
    if [ $last_eroror -eq 0 ]
    then 
	   $BZIP2 ${file_name};
    else 
	   rm -rf ${file_name};
    fi
    return $last_eroror;
}

function db_dump_tables () {
    local db_name=$1
    local tables=$2
    local file_name=$3
    ${MDUMP} --user=${DBUSER} --password=${DBPASSWORD} --host=${HOST} ${OPTS} ${db_name} $tables 2>${TMP}  > ${file_name}
    last_eroror=$?
    if [ $last_eroror -eq 0 ]
    then 
	   $BZIP2 ${file_name}
    else 
	   rm -rf ${file_name}
    fi
    return $last_eroror
}

function create_directories() {
    [ ! -d "$BACKUPDIR" ] && {
        $MKDIR -p $BACKUPDIR 2>/dev/null;
        [ "$?" -ne 0 ]  && {
            log "Error creating backup_dir. Exiting..";
            exit;
        }
    }
}

function usage() {
    echo "Usage:  `basename $0` -m dump -u user -p password -d database -t tables";
    echo -e "\t`basename $0` -m dumpall -u user -p password";
}


function do_action(){
    [ -z "$DBUSER" ] && {
            log "Empty DBUSER. Exiting..." ;
            exit ;
    }

    [ -z "$DBPASSWORD" ] && {
            log "Empty DBPASSWORD. Exiting..." ;
            exit ;
    }

    [ -z "$DATABASE" ] && {
            # --all-databases            
            log "Empty DATABASE. Exiting..." ;
            exit; 
    }
    create_directories;    

    [ $? -eq 0 ] && {
        log "Exit. To often.."
        exit
    }
    DATABASE=`echo $DATABASE| $SED 's/\,/ /g'`; 
    DBCOUNT=`echo $DATABASE | $WC -w`;
    TABLES=`echo $TABLES | $SED 's/\,/ /g'`; 
    TABLESCOUNT=`echo $TABLES | $WC -w`;
    log "user: $DBUSER ; password: $DBPASSWORD ; databases: $DATABASE ; DB count: $DBCOUNT ; tables: $TABLES; TABLES count: $TABLESCOUNT";
    if [ "$DBCOUNT" -eq 1 ]
    then    
        db_dump_tables "$DATABASE" "$TABLES" "${BACKUPDIR}/${DATABASE}-${DATE}.sql"
        if [ $? -eq 0 ]; 
        then 
            log "Done! DB: ${DATABASE}-${DATE}.sql.bz2. DATE: ${DATE}";
        else
            log "ERROR making backup. DB: ${DATABASE}. DATE: ${DATE}. `cat $TMP`";
            rm -rf ${TMP};            
        fi
    elif [ "$DBCOUNT" -gt 1 ]
    then
        for db in $DATABASE
        do
            db_dump "$db" "${BACKUPDIR}/${db}-${DATE}.sql"
            if [ $? -eq 0 ]; 
            then 
                log "Done! DB: ${db}-${DATE}.sql.bz2 DATE: ${DATE}";
            else
                log "ERROR making backup. DB: ${db}. DATE: ${DATE}. `cat ${TMP}`";
                rm -rf ${TMP};
            fi
        done
#   else
#      echo "Don't know what to do... :("
    fi
    echo "$SECONDS" > $TIMEMARKFILE
}

while [ "$1" != "" ]; do
  case $1 in
    -m )  shift
        action=$1
        ;;
    -u ) shift
        DBUSER=$1
        ;;
    -p ) shift
        DBPASSWORD=$1
        ;;
    -d ) shift
        DATABASE=$1
        ;;
    -t ) shift
        TABLES=$1
        ;;
    *)  usage
        exit 1
  esac
  shift
done

case $action in
    "dump" )
        do_action
        ;;
    "dumpall" )
        dumpall
        ;;
    *) usage $1
    exit 1
esac
