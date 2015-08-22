--Function that add some languages to the Language table if it's empty of records
create or replace function check_languages_existence()
  RETURNS INTEGER AS'
BEGIN 
  IF (select default_name from LANGUAGE where default_name = ''English'') IS NULL THEN
      insert into LANGUAGE(default_name) VALUES (''English''), (''Russian''), (''German'');
  END IF;
  RETURN 3;
END;
' LANGUAGE plpgsql;

select check_languages_existence();

--Updates fields last_lesson_add_date and lessons_count of a cluster
--when a new lesson is added
create or replace function update_last_lesson_add_date()
  returns trigger as'
  begin
    update "cluster"
      set last_lesson_add_date = now(), lessons_count = lessons_count + 1
      where cluster_id = NEW.cluster_id;
    return NEW;
  end;
' LANGUAGE plpgsql;

drop trigger if exists update_last_lesson_add_date_for_cluster on lesson;
create trigger update_last_lesson_add_date_for_cluster
  after insert
  on lesson
  for each row
  execute procedure update_last_lesson_add_date();


--Updates field lessons_count of a cluster when a lesson is removed
create or replace function update_lessons_count_when_lesson_deleted()
  returns trigger as'
  begin
    update "cluster"
      set lessons_count = lessons_count - 1
      where cluster_id = OLD.cluster_id;
    return NEW;
  end;
' LANGUAGE plpgsql;

drop TRIGGER if exists update_lessons_count_when_lesson_deleted_trigger on lesson;
create trigger update_lessons_count_when_lesson_deleted_trigger
  after delete
  on lesson
  for each row
  execute procedure update_lessons_count_when_lesson_deleted();







