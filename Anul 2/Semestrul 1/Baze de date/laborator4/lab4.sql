use LumeaCartilor
go

insert into Tables (Name) values ('Biblioteci')
insert into Tables (Name) values ('Carti')
insert into Tables (Name) values ('CartiBiblioteci')
select * from Tables
go

--- afiseaza Bibliotecile unde nr. cititori > 70
create view View1 as 
select * from Biblioteci
where NrCititori > 70;
go

select * from View1
go

--- afiseaza Bibliotecile cu orarul mentionat si adresa lor
create or alter view View2 as
select b.IdBiblioteca, b.OrarBiblioteca, b.AdresaB
from Biblioteci b inner join CartiBiblioteci cb on b.IdBiblioteca = cb.IdBiblioteca
where b.OrarBiblioteca = '08:30 - 20:30' and b.AdresaB like 'Zona2%'
go

select * from View2
go

-- inner join pentru relatia m-n Carti - CartiBiblioteci - Biblioteci
-- afiseaza cartile a caror Titlu e de forma data
-- si se afla in bibliotecile cu nr. cititori mai mare de 150
create or alter view View3 as
select c.IdCarte, c.Titlu, b.AdresaB, b.NrCititori
from Carti c inner join Biblioteci b on b.IdBiblioteca = c.IdCarte
where b.NrCititori = 150 and c.Titlu like 'Titlu%'
group by c.IdCarte, c.Titlu, b.AdresaB, b.NrCititori
go

select * from Biblioteci
select * from Carti
select * from CartiBiblioteci

select *from View3
go

insert into Views (Name) values ('View1')
insert into Views (Name) values ('View2')
insert into Views (Name) values ('View3')
go


insert into Tests (Name) values ('delete_table_1')
insert into Tests (Name) values ('insert_table_1')
insert into Tests (Name) values ('select_view_1')

insert into Tests (Name) values ('delete_table_2')
insert into Tests (Name) values ('insert_table_2')
insert into Tests (Name) values ('select_view_2')

insert into Tests (Name) values ('delete_table_3')
insert into Tests (Name) values ('insert_table_3')
insert into Tests (Name) values ('select_view_3')


insert into TestViews (TestID, ViewID) values (3, 1), (6, 2), (9, 3)

select * from TestTables
select * from Tables
select * from Tests
select * from Views
select * from TestTables
select * from TestViews

insert into TestTables(TestID, TableID, NoOfRows, Position) values (1, 1, 1000, 1)
insert into TestTables(TestID, TableID, NoOfRows, Position) values (2, 1, 100, 2)
insert into TestTables(TestID, TableID, NoOfRows, Position) values (3, 1, 100, 3)

insert into TestTables(TestID, TableID, NoOfRows, Position) values (4, 2, 1000, 4)
insert into TestTables(TestID, TableID, NoOfRows, Position) values (5, 2, 100, 5)
insert into TestTables(TestID, TableID, NoOfRows, Position) values (6, 2, 100, 6)

insert into TestTables(TestID, TableID, NoOfRows, Position) values (7, 3, 1000, 7)
insert into TestTables(TestID, TableID, NoOfRows, Position) values (8, 3, 100, 8)
insert into TestTables(TestID, TableID, NoOfRows, Position) values (9, 3, 100, 9)

select * from TestTables

create or alter procedure select_view_1
as
begin
	 select * from View1
end
go

create or alter procedure select_view_2
as
begin
	 select * from View2
end
go

create or alter procedure select_view_3
as
begin
	 select * from View3
end
go

exec select_view_1
exec select_view_2
exec select_view_3


create or alter procedure insert_table_1
as 
begin 
	declare @NoOfRows int
	declare @n int
	declare @titlu varchar(100)
	declare @autor varchar(100)
	declare @fk int 

	select top 1 @fk = IdGen from GenulCartii
	select top 1 @NoOfRows = NoOfRows from dbo.TestTables
	set @n = 1

	while @n<@NoOfRows
	begin 
		set @titlu = 'Titlu' + convert (varchar(5), @n)
		set @autor = 'Autor' + convert (varchar(5), @n)
		---insert into  Carti (IdCarte, Titlu, Autor, NrSerie, NrPagini, IdGen) values (@n, @titlu, @autor, 10000, 370, @fk)
		insert into  Carti (Titlu, Autor, NrSerie, NrPagini, IdGen) values (@titlu, @autor, 10000, 370, @fk)
		set @n=@n+1
	end
end
go

exec insert_table_1

insert into  Carti (Titlu, Autor, NrSerie, NrPagini, IdGen) values ('Titlu', 'autor', 10000, 370, 2)
select * from Carti


create or alter procedure insert_table_2
as 
begin 
	declare @NoOfRows int
	declare @n int
	declare @adresab varchar(100)

	select top 1 @NoOfRows = NoOfRows from dbo.TestTables
	set @n = 1

	while @n<@NoOfRows
	begin 
		set @adresab = 'Zona' + convert (varchar(5), @n)
		insert into Biblioteci(AdresaB, OrarBiblioteca, NrCititori) values (@adresab, '08:30 - 20:30', 150)
		set @n=@n+1
	end
end
go

exec insert_table_2

select * from Biblioteci
go

create or alter procedure insert_table_3
as
begin
	insert into CartiBiblioteci
	select IdCarte, IdBiblioteca
	from Carti CROSS JOIN Biblioteci 
end
go

exec insert_table_3
go

create or alter procedure delete_table_1
as
begin 
	
	delete from Carti where Titlu like 'Titlu%'
end;
go

exec delete_table_1


create procedure delete_table_2
as
begin 
	delete from Biblioteci where AdresaB like 'Zona%'
end;
go


create procedure delete_table_3
as
begin 
	delete from CartiBiblioteci
end;
go

insert into CartiBiblioteci(IdBiblioteca, IdCarte) values (1, 4), (1, 5), (2, 3), (4, 5), (3, 1)
exec delete_table_3

select * from Carti
select * from Biblioteci
select * from CartiBiblioteci
go

create or alter procedure main_testing
as
begin 

    declare @ds datetime
    declare @di datetime
    declare @de datetime
    declare @i int
    declare @nrTests int
    declare @delete varchar(100)
    declare @insert varchar(100)
    declare @view varchar(100)
    declare @table varchar(100)
    declare @index int
    declare @idTable int

    set @i = 1

    select @nrTests = count(*) from Tests

    while @i <= @nrTests
    begin

        select @table = t.Name from TestTables testTable INNER JOIN Tables t ON testTable.TableID = t.TableID WHERE testTable.TestID = @i
        select @idTable = TableID from TestTables where TestID = @i

        select @delete = Name from Tests where TestID = @i
        set @i = @i + 1

        select @insert = Name from Tests where TestID = @i
        set @i = @i + 1

        select @view = Name from Tests where TestID = @i
        set @i = @i + 1

        set @ds = getdate()

        exec @delete
        exec @insert

        set @di = getdate()

        exec @view

        set @de = getdate()

        print DATEDIFF(ms, @ds, @de)

        insert into TestRuns(Description, StartAt, EndAt) values ('Test-Table: ' + @table, @ds, @de)

        select @index = max(TestRunID) from TestRuns

        insert into TestRunTables(TestRunID, TableID, StartAt, EndAt) values (@index, @idTable, @ds, @di)

        insert into TestRunViews(TestRunID, ViewID, StartAt, EndAt) values (@index, @idTable, @di, @de)
	end
end

exec main_testing

select * from TestRunTables
select * from TestRunViews
select * from TestRuns