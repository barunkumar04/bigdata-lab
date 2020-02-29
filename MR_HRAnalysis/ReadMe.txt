
## KPI 1 - Average satisfaction_level for individual Department.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI01_DeptWiseAvgSatisfactionLevelJob /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI01_DeptWiseAvgSatisfactionLevelJob/
Output screenshot: output/KPI01_DeptWiseAvgSatisfactionLevelJob.png

## KPI 2 - How many employees are left in each individual Department?
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI02_DeptWiseEmployeeResignationJob /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI02_DeptWiseEmployeeResignationJob/
Output screenshot: output/KPI02_DeptWiseEmployeeResignationJob.png


## KPI 3 - Department wise average monthly working hour.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI03_DeptWiseAvgMonthlyHourJob /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI03_DeptWiseAvgMonthlyHourJob/
Output screenshot: output/KPI03_DeptWiseAvgMonthlyHourJob.png

## KPI 4 - No of Project done by individual Department.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI04_DeptWiseProjectDone /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI04_DeptWiseProjectDone/
Output screenshot: output/KPI04_DeptWiseProjectDone.png

## KPI 5 - Department wise salary Distribution.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI05_DepartmentWiseSalaryDistribution /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI05_DepartmentWiseSalaryDistribution/
Output screenshot: output/KPI05_DepartmentWiseSalaryDistribution.png

## KPI 6 -  In individual Department How many Employees promoted in last 5 years but still left the company.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI06_DeptWisePromotedButResignedJob /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI06_DeptWisePromotedButResignedJob/
Output screenshot: output/KPI06_DeptWisePromotedButResignedJob.png

## KPI 7 -  Department wise average satisfaction level, average working hours and no of employee who left company
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI07_DeptWiseReportJob /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI07_DeptWiseReportJob/
Output screenshot: output/KPI07_DeptWiseReportJob.png

## KPI 8 -  When salary is low find out the Department wise mean satisfaction level , average working hours and no of employee who left company
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI08_DeptWiseReportJobWhenSalaryIsLow /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI08_DeptWiseReportJobWhenSalaryIsLow/
Output screenshot: output/KPI08_DeptWiseReportJobWhenSalaryIsLow.png

## KPI 9 -  When salary is low and not promoted in last 5 year than find out the Department wise mean satisfaction level, average working hours and no of employee who left company
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI09_DeptWiseReportJobWhenSalaryIsLowAndNotPromoted /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI09_DeptWiseReportJobWhenSalaryIsLowAndNotPromoted/
Output screenshot: output/KPI09_DeptWiseReportJobWhenSalaryIsLowAndNotPromoted.png

## KPI 10 -  For individual department find out the average satisfaction_level, average evaluation and percentage of employees left company on the basis of salary.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI10_DeptWiseReportOnBasisOfSalaryJob /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI10_DeptWiseReportOnBasisOfSalaryJob/
Output screenshot: output/KPI10_DeptWiseReportOnBasisOfSalaryJob.png

## KPI 11 -  How many employees left, distribution based on experience
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI11_EmployeeLeftDistributionOnExp /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI11_EmployeeLeftDistributionOnExp/
Output screenshot: output/KPI11_EmployeeLeftDistributionOnExp.png

## KPI 12 -  Name of the department where more than 70% employees left the company.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI12_DeptWhere70PercentEmployeeResigned /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI12_DeptWhere70PercentEmployeeResigned/
Output screenshot: output/KPI12_DeptWhere70PercentEmployeeResigned.png

## KPI 13 -  Highly experienced employee in each department.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI13_HighlyExperiencedEmployeeInEachDepartment /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI13_HighlyExperiencedEmployeeInEachDepartment/
Output screenshot: output/KPI13_HighlyExperiencedEmployeeInEachDepartment.png

## KPI 14 -  Salary Distribution of highly experienced employee in company.
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI14_SalaryDistributionOfHighlyExperiencedEmployee /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI14_SalaryDistributionOfHighlyExperiencedEmployee/
Output screenshot: output/KPI14_SalaryDistributionOfHighlyExperiencedEmployee.png

## KPI 15 -   In which department total no of project is greater than 40% of overall project
Command: yarn jar /home/barun/HadoopWorkshopArtifacts/HRDataAnalysis/HRDataAnalysis.jar com.hadoop.mr.hr.analytics.KPI15_DepartmentTotalProjectIsMoreThan40Percent /HRDataAnalysis/Input/HR_Analysis_Dataset.csv /HRDataAnalysis/Output/KPI15_DepartmentTotalProjectIsMoreThan40Percent/
Output screenshot: output/KPI15_DepartmentTotalProjectIsMoreThan40Percent.png

