use LumeaCartilor
go

create table versiune (
idVersiune int primary key default 0, 
numarVersiune int
)
go

insert into versiune (numarVersiune) values (0)
go

select * from versiune
go

--- procedura 1 -> modifica coloana NrCartiCuAcelasiGen din int in tinyint

create or alter procedure do_proc_1
as
begin
	alter table GenulCartii
	alter column NrCartiCuAcelasiGen tinyint NOT NULL

	print 'Coloana NrCartiCuAcelasiGen este acum tinyint'
	print 'Baza de date se afla acum la versiunea 1'
	select * from GenulCartii
end
go

--- procedura2 -> operatia inversa procedurii 1
---				  NrCartiCuAcelasiGen din tinyint in int

create or alter procedure undo_proc_1
as
begin 
	alter table GenulCartii
	alter column NrCartiCuAcelasiGen int

	print 'Coloana NrCartiCuAcelasiGen este acum int.'
	print 'Baza de date se afla acum la versiunea 0.'
end
go

--- procedura3 -> adauga o constrangere de valoare implicita 
---				  pentru coloana NrSerie din tabela Carti
---				  default 10000

create or alter procedure do_proc_2
as
begin
	alter table Carti
	add constraint df_10000 default 10000
	for NrSerie

	print 'Coloana NrSerie are o constragere DEFAULT pentru coloana NrSerie.'
	print 'Baza de date se afla acum la versiunea 2.'
end
go


--- procedura4 -> operatia inversa procedurii 2
---				  se sterge constrangerea default de 10000

create or alter procedure undo_proc_2
as
begin
	alter table Carti
	drop constraint df_10000

	print 'S-a sters constrangerea de tip DEFAULT pentru NrSerie.'
	print 'Baza de date se afla acum la versiunea 1'
end
go


--- procedura5 -> creeaza o tabela Manuale cu campurile
---               IdManual, TipManual, Coperta(culoare)

create or alter procedure do_proc_3
as
begin
	create table Manuale (
	IdManual int primary key identity NOT NULL,
	TipManual varchar(100) NOT NULL,
	Coperta varchar(100)
	) 

	print 'S-a creat cu succes tabela Manuale.'
	print 'Baza de date se afla acum la versiunea 3.'
end
go

--- procedura6 -> operatia inversa procedurii 3
---               se sterge tabela Manuale

create or alter procedure undo_proc_3
as
begin
	drop table Manuale

	print 'Tabela Manuale a fost stearsa!'
	print 'Baza de date se afla acum la versiunea 2.'
end 
go

--- procedura7 -> adauga o noua coloana (NrPagini) 
---               in tabela Manuale

create or alter procedure do_proc_4
as 
begin
	alter table Manuale
	add NrPagini int

	print 'Am adaugat coloana NrPagini pentru Manuale.'
	print 'Baza de date se afla acum la versiunea 4.'
end
go

---  procedura8 -> operatia inversa procedurii 4
---				   sterge coloana NrPagini din tabela Manuale

create or alter procedure undo_proc_4
as
begin
	alter table Manuale
	drop column NrPagini

	print 'Coloana NrPagini a fost stearsa.'
	print 'Baza de date se afla acum la versiunea 3.'
end 
go
 
--- procedura9 -> adaugam coloana pe care dorim sa o definim ca si cheie straina
---		          adaugam cheia straina IdBiblioteca

create or alter procedure do_proc_5
as
begin
	alter table Manuale
	add IdBiblioteca int not null

	alter table Manuale
	add constraint fk_Manuale foreign key (IdBiblioteca) references Biblioteci(IdBiblioteca)

	print 'A fost adaugata o constrangere de cheie straina.'
	print 'Baza de date se afla acum la versiunea 5.'
end
go

--- procedura10 -> operatia inversa procedurii 9
---                stergem constrangerea de cheie straina
---                stergem coloana adaugata ulterior

create or alter procedure undo_proc_5
as
begin
	alter table Manuale
	drop constraint fk_Manuale

	alter table Manuale
	drop column IdBiblioteca

	print 'A fost stearsa constrangerea de cheie straina.'
	print 'Baza de date se afla acum la versiunea 4.'
end
go

create or alter procedure main(
@versiune_noua varchar(50)
)
as 
begin
	if isnumeric(@versiune_noua) = 0
		raiserror('Valoare invalida!',12,1);
	else
	begin 
		set @versiune_noua = cast(@versiune_noua as int)
		if @versiune_noua < 0 or @versiune_noua > 5
			raiserror('Nu exista versiunea data!',12,1);
		else
		begin
			declare @versiune int
			select @versiune = numarVersiune from versiune
			
			if @versiune_noua < @versiune
			begin 
				declare @comanda varchar(50);
			    while(@versiune_noua< @versiune)
				begin
					set @comanda = 'undo_proc_' + cast(@versiune as varchar)
					print 'Comanda executata este: ' + @comanda
					exec @comanda
					set @versiune = @versiune - 1
				end;
				update versiune
				set numarVersiune = @versiune
			end;
			else
			begin
				while(@versiune != @versiune_noua)
				begin
					set @versiune = @versiune + 1
					set @comanda = 'do_proc_' + cast(@versiune as varchar)
					print 'Comanda executata este: ' + @comanda
					exec @comanda
				end;
				update versiune
				set numarVersiune = @versiune
			end;
		end;
	end;
end;


exec main 0
exec main 1
exec main 2
exec main 3
exec main 4
exec main 5



select * from versiune
