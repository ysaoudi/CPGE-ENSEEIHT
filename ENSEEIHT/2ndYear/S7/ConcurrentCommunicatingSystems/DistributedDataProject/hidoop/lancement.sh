# Définition des constantes
PATH_REPO='~/2SN/hidoop/src'
SUFFIX='.enseeiht.fr'
CLIENT='targaryen'${SUFFIX}
DAEMON1='r2d2'${SUFFIX}
DAEMON2='nickel'${SUFFIX}
DAEMON3='gandalf'${SUFFIX}
NAMENODE='targaryen'${SUFFIX}

#"r2d2.enseeiht.fr", "nickel.enseeiht.fr", "gandalf.enseeiht.fr"

echo "Lancement du namenode"
ssh msensali@${NAMENODE} "cd ${PATH_REPO} && java hdfs.NameNodeImpl"&
sleep 10
echo "Lancement des HDFSservers"
ssh msensali@${DAEMON1} "cd ${PATH_REPO} && java hdfs.HdfsServer 8001"&
ssh msensali@${DAEMON2} "cd ${PATH_REPO} && java hdfs.HdfsServer 8002"&
ssh msensali@${DAEMON3} "cd ${PATH_REPO} && java hdfs.HdfsServer 8003"&
sleep 5
echo "Lancement des daemons"
ssh msensali@${DAEMON1} "cd ${PATH_REPO} && java ordo.WorkerImpl 8887"&
ssh msensali@${DAEMON2} "cd ${PATH_REPO} && java ordo.WorkerImpl 8888"&
ssh msensali@${DAEMON3} "cd ${PATH_REPO} && java ordo.WorkerImpl 8889"&
sleep 5

echo  Lancement du client
ssh msensali@${CLIENT} "cd ${PATH_REPO} && java hdfs.HdfsClient write line test.txt"
ssh msensali@${CLIENT} "cd ${PATH_REPO} && java application.MyMapReduce test.txt"



