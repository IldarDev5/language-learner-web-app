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