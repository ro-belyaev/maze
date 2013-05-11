DROP TABLE game CASCADE CONSTRAINTS;
DROP SEQUENCE game_seq;


DROP TABLE move CASCADE CONSTRAINTS;
DROP SEQUENCE move_seq;

DROP TABLE player CASCADE CONSTRAINTS;

DROP TABLE treasure CASCADE CONSTRAINTS;

DROP TABLE simple_entity CASCADE CONSTRAINTS;
DROP SEQUENCE simple_entity_seq;

DROP TABLE portal_entity CASCADE CONSTRAINTS;
DROP SEQUENCE portal_seq;


CREATE TABLE game
(
	id NUMBER CONSTRAINT game_id_nn NOT NULL,
	num_of_players NUMBER CONSTRAINT check_num_of_players CHECK (num_of_players > 0),
	num_of_treasures NUMBER,
	size_x NUMBER NOT NULL,
	size_y NUMBER NOT NULL,
	CONSTRAINT game_pk
	    PRIMARY KEY (id)
);

CREATE SEQUENCE game_seq
START WITH 0
INCREMENT BY 1
MINVALUE 0
NOMAXVALUE;

create or replace trigger game_id_trg
before insert on game
for each row
begin
    if :new.id is null then
	select game_seq.nextval into :new.id from dual;
    end if;
end;
/
------------------------------------------------

CREATE TABLE move
(
	game_id NUMBER,
	move_id NUMBER,
	cur_player_id NUMBER,
	game_state VARCHAR2(10) CHECK(game_state IN ('process', 'end')),
	action VARCHAR2(14) CHECK(action IN ('start_game', 'terminate', 'go', 'take_treasure', 'shoot', 'drop_treasure', 'ask_prediction', 'exit')) NOT NULL,
	action_direction VARCHAR2(10) CHECK(action_direction IN ('left', 'right', 'up', 'down')),
	CONSTRAINT move_pk
	    PRIMARY KEY (game_id, move_id),
	CONSTRAINT game_id_fk
	    FOREIGN KEY (game_id)
	    REFERENCES game (id)
);


CREATE SEQUENCE move_seq
START WITH 0
INCREMENT BY 1
MINVALUE 0
NOMAXVALUE;


create or replace trigger move_id_trg
before insert on move
for each row
begin
    if :new.move_id is null then
	select move_seq.nextval into :new.move_id from dual;
    end if;
end;
/
------------------------------------------------

CREATE TABLE player
(
	game_id NUMBER,
	move_id NUMBER,
	player_id NUMBER,
	name VARCHAR2(20),
	position_x NUMBER NOT NULL,
	position_y NUMBER NOT NULL,
	num_of_health NUMBER NOT NULL,
	num_of_shots NUMBER NOT NULL,
	state VARCHAR2(15) CHECK(state IN ('alive', 'dead', 'winner', 'exit_from_maze', 'loser')) NOT NULL,
	CONSTRAINT player_pk
	    PRIMARY KEY (game_id, move_id, player_id),
/*	CONSTRAINT game_id_fk     */
/*	    FOREIGN KEY (game_id) */
/*	    REFERENCES game (id), */
	CONSTRAINT move_id_fk
	    FOREIGN KEY (game_id, move_id)
	    REFERENCES move (game_id, move_id)
);

-------------------------------------------------

CREATE TABLE treasure
(
	game_id NUMBER,
	move_id NUMBER,
	color NUMBER,
	x_position NUMBER,
	y_position NUMBER,
	state VARCHAR2(10) CHECK(state IN ('found', 'not_found')),
	owner_id NUMBER,
	worth VARCHAR2(10) CHECK(worth IN ('real', 'fake')),
	CONSTRAINT treasure_fk
	    FOREIGN KEY (game_id, move_id)
	    REFERENCES move (game_id, move_id),
	CONSTRAINT treasure_pk
	    PRIMARY KEY (game_id, move_id, color)
);

----------------------------------------------------

CREATE TABLE simple_entity
(
	game_id NUMBER,
	entity_id NUMBER,
	entity_type VARCHAR2(10) CHECK(entity_type IN ('arch', 'exit', 'mage')),
	position_x NUMBER,
	position_y NUMBER,
	CONSTRAINT simple_entity_pk
	    PRIMARY KEY (game_id, entity_id),
	CONSTRAINT entity_fk
	    FOREIGN KEY (game_id)
	    REFERENCES game (id)
);


CREATE SEQUENCE simple_entity_seq
START WITH 0
INCREMENT BY 1
MINVALUE 0
NOMAXVALUE;


create or replace trigger simple_entity_id_trg
before insert on simple_entity
for each row
begin
    if :new.entity_id is null then
	select simple_entity_seq.nextval into :new.entity_id from dual;
    end if;
end;
/

----------------------------------------------------

CREATE TABLE portal_entity
(
	game_id NUMBER,
	portal_id NUMBER,
	entity_type VARCHAR2(10) CHECK(entity_type IN ('wall', 'river')) NOT NULL,
	start_x NUMBER NOT NULL,
	start_y NUMBER NOT NULL,
	dest_x NUMBER NOT NULL,
	dest_y NUMBER NOT NULL,
	step NUMBER NOT NULL,
	CONSTRAINT portal_pk
	    PRIMARY KEY (game_id, portal_id),
	CONSTRAINT portal_fk
	    FOREIGN KEY (game_id)
	    REFERENCES game (id)
);

CREATE SEQUENCE portal_seq
START WITH 0
INCREMENT BY 1
MINVALUE 0
NOMAXVALUE;

create or replace trigger portal_trg
before insert on portal_entity
for each row
begin
    if :new.portal_id is null then
	select portal_seq.nextval into :new.portal_id from dual;
    end if;
end;
/
