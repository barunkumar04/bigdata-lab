###########################################################
### KPI1: Command used to invoke MR job to parse weblog
###########################################################

yarn jar /home/barun/HadoopWorkshopArtifacts/Weblog/WeblogParserJob.jar com.hadoop.mr.weblog.WeblogParserJob /weblog/input/ /weblog/output

#Output screenshot location:  MR_WelogAnalysis/output/KPI1_parsed_weblogs.PNG

###########################################################
### Prerequisites for KPI 2 & 3
###########################################################

## Creating hive table to store parsed weblogs

CREATE EXTERNAL TABLE weblogs(
remoteIP STRING,
remotelogname STRING,
user STRING,
time STRING,
request_type STRING,
request_string STRING,
category_1 STRING,
category_2 STRING,
category_3 STRING,
page STRING,
param STRING,
status_code STRING,
byte_string STRING,
user_agent STRING,
referral STRING
)
row format delimited
fields TERMINATED by '\t';


## Coping parsed weblog to unix file system and loading to weblog table. This is avoid deleting the source file.

hdfs dfs -copyToLocal /weblog/output/ParsedRecords-m-00000 /home/barun/

## Loading parsed weblogs to hive 'weblogs' table

LOAD DATA LOCAL INPATH '/home/barun//ParsedRecords-m-00000' OVERWRITE INTO TABLE weblogs;

##################################################################
### KPI2: Count of page views by individual user using hive query
##################################################################

# select remoteIP, count(*) from weblogs group by remoteIP;

# Output screenshot location: MR_WelogAnalysis/output/KPI2_user_wise_page_distribution.PNG

#######################################################################
### KPI3: Category(cat-1) wise page-visit distribution using hive query
#######################################################################

# select category_1, count(1) from weblogs group by category_1; 

# Output screenshot location: MR_WelogAnalysis/output/KPI3_cat_1_wise_page_distribution.PNG
