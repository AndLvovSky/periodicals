drop sequence publication_sequence;
drop sequence user_sequence;
create sequence publication_sequence start with 1 increment by 1;
create sequence user_sequence start with 1 increment by 1;
delete from publication where true;
insert into publication (id ,name, period, cost, description) values
    (1, 'Tampa Bay Times', 7, 5., '-'),
    (2, 'Minneapolis Star Tribune', 30, 7.5, '-'),
    (3, 'Philadelphia Inquirer', 7, 10., '-'),
    (4, 'St. Louis Post-Dispatch', 7, 3.75, '-'),
    (5, 'New Jersey Star-Ledger', 14, 11., '-'),
    (6, 'Arizona Republic', 21, 16.2, '-');
alter sequence publication_sequence restart with 7;