SET SQL_SAFE_UPDATES = 0;
update staff set avatar=concat('attendance/',emp_id,'_',name,'.jpg');
SET SQL_SAFE_UPDATES = 1;