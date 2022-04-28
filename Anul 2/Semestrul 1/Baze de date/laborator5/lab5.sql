use LumeaCartilor
go

create or alter function dbo.validareParametriiCititori(@nume varchar(100), @prenume varchar(100), @varsta varchar(100), @nrTelefon varchar(100), @email varchar(100))
returns varchar(300)
as
begin	
	declare @err varchar(300)
	set @err = ''
	if (@nume = '')
		select @err = @err + 'Numele nu poate fi vid!'
	if (@prenume = '')
		select @err = @err + 'Prenumele nu poate fi vid!' 
	if (ISNUMERIC(@varsta) = 0)
		select @err = @err + 'Varsta trebuie sa aiba o valoare numerica!'
	else if(cast (@varsta as int) <= 10 or @varsta > 90)
		select @err = @err + 'Varsta trebuie sa fie cuprinsa intre 10 si 90!'
	if (ISNUMERIC(@nrTelefon) = 0)
		select @err = @err + 'Nr. de telefon trebuie sa aiba o valoare numerica!'
	else if (cast (@nrTelefon as bigint) not like '%[^0-9]%' and LEN(@nrTelefon) != 10)
		select @err = @err + 'Nr. de telefon trebuie sa aiba 10 cifre!'
	if (@email= '')
		select @err = @err + 'Email nu poate fi vid!'
	if (@email not like '%@mail.com')
		select @err = @err + 'Email invalid!'
	return @err

end 
go

print(dbo.validareParametriiCititori ('', 'prenume', 15, 2999119290, 'mariaail.com'))
go


create or alter function dbo.validareParametriiCartiImprum(@dataImprum varchar(100), @dataReturn varchar(100), @nrCopiiCarti varchar(100))
returns varchar(300)
as
begin	
	declare @err varchar(300)
	set @err = ''
	if (@dataImprum = '')
		select @err = @err + 'Data imprumutului nu poate sa fie vida!'
	if (@dataReturn = '')
		select @err = @err + 'Data returnarii nu poate sa fie vida!' 
	if (ISNUMERIC(@nrCopiiCarti) = 0)
		select @err = @err + 'Numarul de copii al cartilor trebuie sa aiba o valoare numerica!'
	else if(cast (@nrCopiiCarti as int) <= 0 or @nrCopiiCarti > 100)
		select @err = @err + 'Numarul de copii al cartilor trebuie sa fie cuprins intre 0 si 100!'
	return @err
end 
go

create or alter function dbo.validareParametrii(@idCarteImprum varchar(100), @idCititor varchar(100), @nrCartiImprum varchar(100))
returns varchar(300)
as
begin	
	declare @err varchar(300)
	set @err = ''
	if (@idCarteImprum = '')
		select @err = @err + 'Id-ul cartii imprumutate nu poate sa fie vid!'
	if (@idCititor = '')
		select @err = @err + 'Id-ul cititorului nu poate sa fie vid!'
	if (ISNUMERIC(@nrCartiImprum) = 0)
		select @err = @err + 'Numarul de carti trebuie sa aiba o valoare numerica!'
	else if(cast (@nrCartiImprum as int) <= 0 and @nrCartiImprum > 20)
		select @err = @err + 'Numarul de carti trebuie sa fie cuprins intre 0 si 20!'
	if not exists(select IdCarteImprum from CartiImprumutate where IdCarteImprum = @idCarteImprum)
		select @err = @err + 'Id-ul cartii imprumutate nu exista!'
	if not exists(select IdCititor from Cititori where IdCititor = @idCititor)
		select @err = @err + 'Id-ul cititorului nu exista!'
	if exists (select IdCarteImprum, IdCititor from CititoriCartiImprumutate where IdCarteImprum = @idCarteImprum and IdCititor = @idCititor)
		select @err = 'Aceasta inregistrare exista deja si nu se poate insera in tabel!'
	return @err
end 
go

create or alter procedure Crud_Cititori
	@table_name varchar(50),
	@nume varchar(50),
	@prenume varchar(50),
	@varsta int,
	@nr_telefon bigint, 
	@email varchar(50),
	@noOfRows int,
	@message varchar(300) output
as
begin
	set nocount on;

	declare @varstaInt int
	declare @nrTelefInt bigint
	declare @verify varchar(100)

	set @verify = dbo.validareParametriiCititori(@nume, @prenume, @varsta, @nr_telefon, @email)
	if (@verify = '')
	begin
		set @varstaInt = convert(int, @varsta)
		set @nrTelefInt = convert(bigint, @nr_telefon)

	--insert into table = create
		declare @n int = 1
		while @n < @noOfRows begin
			insert into Cititori(Nume, Prenume, Varsta, NrDeTelefon, Email)
				values (@nume, @prenume, @varstaInt, @nrTelefInt, @email)
			print ('Inserarea in tabelul Cititori a fost cu succes!')
			set @n = @n + 1
		end

	--read = select
		select * from Cititori
		print('Operatia de citire a fost cu succes!')

	--update
		update Cititori set Email = 'yahoo@mail.com' where Varsta > 19 --and Prenume like 'M%'
		print('In tabelul Cititori s-au modificat parametrii corespunzatori cu succes!')
	--delete
		delete from Cititori where Varsta = 24
		print('Operatia de stergere pentru Cititori a fost cu succes!')

		--print 'CRUD operations for table ' + @table_name
		set @message = 'Operatiile CRUD pentru tabelul ' + @table_name + ' au fost efectuate cu succes!'

	end
	else
		set @message = @verify
end

declare @erori varchar(1000)
exec Crud_Cititori 'Cititori', 'Vuscan', 'Ale', 27, 1254214521, 'ale@mail.com', 2, @erori output
print (@erori)
exec Crud_Cititori 'Cititori', 'Fodorean', 'Anda', 20, 125356657, 'andamail.com', 1, @erori output
print (@erori)
exec Crud_Cititori 'Cititori', 'Jucan', 'Diana', 24, 1253470457, '', 1, @erori output
print (@erori)

delete from Cititori where Nume = 'Vuscan'
update Cititori set Email = 'stanciu@mail.com' where Nume = 'Stanciu'
select * from Cititori
go


create or alter procedure Crud_CartiImprumutate
	@table_name varchar(50),
	@dataImprum varchar(50),
	@dataReturn varchar(50),
	@nrCopiiCarti int,
	@noOfRows int,
	@message varchar(300) output
as
begin
	set nocount on;

	declare @nrCartiInt int
	declare @verify varchar(100)
	
	set @verify = dbo.validareParametriiCartiImprum(@dataImprum, @dataReturn, @nrCopiiCarti)
	if (@verify = '')
	begin
		set @nrCartiInt = convert(int, @nrCopiiCarti)
	--insert into table = create
		declare @n int = 1
		while @n < @noOfRows begin
			insert into CartiImprumutate(DataImprumut, DataReturn, nrCopiiDeCarti)
				values (@dataImprum, @dataReturn, @nrCopiiCarti)
				print ('Inserarea in tabelul CartiImprumutate a fost cu succes!')
			set @n = @n + 1
		end

		--read = select
		select * from CartiImprumutate
		print('Operatia de citire a fost cu succes!')

		--update
		update CartiImprumutate set DataReturn = '05.01.2022' where DataImprumut = '15.12.2021'
		print('In tabelul CartiImprumutate s-au modificat parametrii corespunzatori cu succes!')

		--delete
		delete from CartiImprumutate where nrCopiiDeCarti > 16
		print('Operatia de stergere pentru CartiImprumutate a fost cu succes!')

		--print 'CRUD operations for table ' + @table_name
		set @message = 'Operatiile CRUD pentru tabelul ' + @table_name + 'au fost efectuate cu succes!'

	end
	else
		set @message = @verify
		
end

declare @erori varchar(1000)
exec Crud_CartiImprumutate 'CartiImprumutate', '15.12.2021', '23.12.2021', 18, 5, @erori output
print (@erori)
exec Crud_CartiImprumutate 'CartiImprumutate', '20.12.2021', '28.12.2021', 14, 2, @erori output
print (@erori)
exec Crud_CartiImprumutate 'CartiImprumutate', '', '28.12.2021', 105, 2, @erori output
print (@erori)


select * from CartiImprumutate
go


create or alter procedure Crud_CititoriCartiImprumutate
	@table_name varchar(50),
	@fkCarteImprum int,
	@fkCititor int, 
	@nrCartiImprumutate int,
	@noOfRows int,
	@message varchar(300) output
as
begin
	set nocount on;

	declare @carteImprum int
	declare @cititor int
	declare @nrCartiInt int
	declare @verify varchar(100)

	set @verify = dbo.validareParametrii(@fkCarteImprum, @fkCititor, @nrCartiImprumutate)
	if (@verify = '')
	begin
	--insert into table = create
		set @carteImprum = convert(int, @fkCarteImprum)
		set @cititor = convert(int, @fkCititor)
		set @nrCartiInt = convert(int, @nrCartiImprumutate)

		declare @n int = 1
		while @n < @noOfRows begin
	
			insert into CititoriCartiImprumutate values (@carteImprum, @cititor, @nrCartiInt)
			print ('Inserarea in tabelul CititoriCartiImprumutate a fost cu succes!')
			set @n = @n + 1
		end

		--read = select
		select * from CititoriCartiImprumutate
		print('Operatia de citire a fost cu succes!')

		--delete
		delete from CititoriCartiImprumutate where NrCartiImprumutate > 15
		print('Operatia de stergere pentru CititoriCartiImprumutate a fost cu succes!')

		--print 'CRUD operations for table ' + @table_name
		set @message = 'Operatiile CRUD pentru tabelul ' + @table_name + 'au fost efectuate cu succes!'

	end
	else
		set @message = @verify

end


declare @erori varchar(1000)
exec Crud_CititoriCartiImprumutate 'CititoriCartiImprumutate', 4, 1, 5, 2, @erori output
print (@erori)
exec Crud_CititoriCartiImprumutate 'CititoriCartiImprumutate', 4, 3, 5, 2, @erori output
print (@erori)
exec Crud_CititoriCartiImprumutate 'CititoriCartiImprumutate', 5, 2, 6, 2, @erori output
print (@erori)

select * from Cititori
select * from CartiImprumutate
select * from CititoriCartiImprumutate



-- view pe tabelele Cititori si CititoriCartImprumutate
-- care afiseaza cititorii a caror mail incepe cu litera s

create or alter view viewCititori
as
select cit.IdCititor, cit.Nume, cit.Prenume, cit.Varsta, cit.Email from Cititori as cit, CititoriCartiImprumutate as cci
--where cit.IdCititor = cci.IdCititor and cit.Varsta < 40
where cit.IdCititor = cci.IdCititor and cit.Email like 's%@mail.com'
go

-- index pe coloana Email din tabela Cititori
if exists( select name from sys.indexes where name = 'indexCititori')
drop index indexCititori on Cititori
create nonclustered index indexCititori on Cititori(Email)

--update Cititori set Email = 'stanciu@mail.com' where Prenume = 'Sandra'

select * from viewCititori 
order by Email

select * from Cititori
go
select * from CititoriCartiImprumutate

insert into Cititori(Nume, Prenume, Varsta, NrDeTelefon, Email) values ('Stanciu', 'Alina', 23, 0754781474, 'stanciu@mail.com')
insert into CititoriCartiImprumutate(IdCarteImprum, IdCititor, NrCartiImprumutate) values (5, 187, 4)


-- view pe tabelele CartiImprumutate si CititoriCartImprumutate

create or alter view viewCartiImprumutate
as
select ci.IdCarteImprum, ci.nrCopiiDeCarti, cci.NrCartiImprumutate from CartiImprumutate as ci, CititoriCartiImprumutate as cci
where ci.IdCarteImprum = cci.IdCarteImprum and ci.nrCopiiDeCarti = cci.NrCartiImprumutate
go

-- index pe coloana nrCopiiDeCarti din tabela CartiImprumutate
if exists( select name from sys.indexes where name = 'indexCarti')
drop index indexCarti on CartiImprumutate
create nonclustered index indexCarti on CartiImprumutate(nrCopiiDeCarti)

select * from viewCartiImprumutate
order by nrCopiiDeCarti