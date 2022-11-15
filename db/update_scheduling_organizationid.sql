SET SQL_SAFE_UPDATES = 0;
update face_door_record fdc join member m on fdc.staff_id=m.staff_id
    set fdc.organization_id=m.organization_id;

update scheduling_record sr join member m on sr.staff_id=m.staff_id
    set sr.organization_id=m.organization_id;

update staff_scheduling_record scr join member m on scr.staff_id=m.staff_id
    set scr.organization_id=m.organization_id;
SET SQL_SAFE_UPDATES = 1;