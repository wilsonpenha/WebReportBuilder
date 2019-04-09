drop table supervision_report_tmp
create table supervision_report_tmp as select 2 as seq, classificationtagname, username, 
CASE WHEN COLLECT_SET(msg_match_any)[0] IS NULL THEN 0 ELSE COLLECT_SET(msg_match_any)[0] END as msg_match_any, 
CASE WHEN COLLECT_SET(msg_flagged)[0] IS NULL THEN 0 ELSE COLLECT_SET(msg_flagged)[0] END as msg_flagged, 
CASE WHEN COLLECT_SET(msg_state_changed)[0] IS NULL THEN 0 ELSE COLLECT_SET(msg_state_changed)[0] END as msg_state_changed FROM (
select ast.classificationtagname, ast.username, asp.policytype, asp.flagstate, 
    CASE WHEN asp.policytype='PolicyMatch' THEN count(*) END AS msg_match_any,
    CASE WHEN asp.policytype='PolicyMatchFlagAdded' THEN count(*) END AS msg_flagged,
    CASE WHEN asp.policytype='PolicyFlagStateChanged' THEN count(*) END AS msg_state_changed
from avro_supervision_policy asp join avro_supervision_tag ast 
on ast.archiveid=asp.archiveid and ast.serialid=asp.serialid
where asp.year=2018 and asp.month=1 
AND asp.archiveid = 'ff522289-31e7-41fd-8e3c-0c15d3c8f428'
AND asp.functionid = '779427fe-fbcf-403d-af10-c27221141777'
and asp.policytype not in ('PolicyNoMatch') 
group by ast.classificationtagname, ast.username, asp.policytype, asp.flagstate) tbl1
group by classificationtagname, username
union all
select 1 AS seq, 'Total of messages' as classificationtagname, platformname as username, 
CASE WHEN COLLECT_SET(msg_match_any)[0] IS NULL THEN 0 ELSE COLLECT_SET(msg_match_any)[0] END as msg_match_any, 
CASE WHEN COLLECT_SET(msg_flagged)[0] IS NULL THEN 0 ELSE COLLECT_SET(msg_flagged)[0] END as msg_flagged, 
CASE WHEN COLLECT_SET(msg_state_changed)[0] IS NULL THEN 0 ELSE COLLECT_SET(msg_state_changed)[0] END as msg_state_changed FROM (
select platformname, policytype, flagstate, 
    CASE WHEN asp.policytype='PolicyMatch' THEN count(*) END AS msg_match_any,
    CASE WHEN asp.policytype='PolicyMatchFlagAdded' THEN count(*) END AS msg_flagged,
    CASE WHEN asp.policytype='PolicyFlagStateChanged' THEN count(*) END AS msg_state_changed
from avro_supervision_policy asp
where asp.year=2018 and asp.month=1 
AND asp.archiveid = 'ff522289-31e7-41fd-8e3c-0c15d3c8f428'
AND asp.functionid = '779427fe-fbcf-403d-af10-c27221141777'
and asp.policytype not in ('PolicyNoMatch') 
group by asp.platformname, asp.policytype, asp.flagstate) tbl1
group by platformname

