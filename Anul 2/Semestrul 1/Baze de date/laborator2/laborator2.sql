create database LumeaCartilor
go
use LumeaCartilor
go

create table GenulCartii(
IdGen int primary key identity,
Gen varchar(50),
Descriere varchar(100),
NrCartiCuAcelasiGen int
)

create table Carti(
IdCarte int primary key identity, 
Titlu varchar(50) NOT NULL,
Autor varchar(50), 
NrSerie int,
NrPagini int check (NrPagini > 20),
IdGen int foreign key references GenulCartii(IdGen)
)

create table Biblioteci(
IdBiblioteca int primary key identity(1,1),
AdresaB varchar(50),
OrarBiblioteca varchar(50),
NrCititori int
)

create table CartiBiblioteci(
IdBiblioteca int foreign key references Biblioteci(IdBiblioteca),
IdCarte int foreign key references Carti(IdCarte),
constraint pk_CartiBiblioteci primary key (IdBiblioteca, IdCarte)
)

create table AdministratorBiblioteca (
IdAdmin int foreign key references Biblioteci(IdBiblioteca), 
Nume varchar(50),
Prenume varchar(50),
Experienta int, 
constraint pk_AdministratorBiblioteca primary key (IdAdmin)
)

create table Cititori (
IdCititor int primary key identity, 
Nume varchar(50),
Prenume varchar(50),
Varsta int,
NrDeTelefon int,
Email varchar(50),
)

create table CartiImprumutate(
IdCarteImprum int primary key identity,
DataImprumut varchar(50),
DataReturn varchar(50),
nrCopiiDeCarti int
)


create table CategoriiDeVarsta(
IdCategoriiDeVarsta int primary key,
IntervalVarsta varchar(100)
)

create table CititoriCartiImprumutate(
IdCarteImprum int foreign key references CartiImprumutate(IdCarteImprum),
IdCititor int foreign key references Cititori(IdCititor), 
NrCartiImprumutate int,
constraint pk_CititoriCartiImprumutate primary key (IdCarteImprum, IdCititor)
)

create table CititoriCategoriiDeVarsta(
IdCategoriiDeVarsta int foreign key references CategoriiDeVarsta(IdCategoriiDeVarsta),
IdCititor int foreign key references Cititori(IdCititor), 
constraint pk_CititoriCategoriiDeVarsta primary key (IdCategoriiDeVarsta, IdCititor)
)

create table GenulCartiiImprumutate(
IdCarteImprum int foreign key references CartiImprumutate(IdCarteImprum),
IdGen int foreign key references GenulCartii(IdGen)
constraint pk_GenulCartiiImprumutate primary key (IdCarteImprum, IdGen)
)

insert into GenulCartii(Gen, Descriere, NrCartiCuAcelasiGen) values ('Actiune', 'Descriere1','48')
insert into GenulCartii(Gen, Descriere, NrCartiCuAcelasiGen) values ('Dragoste', 'Descriere2', '79')
insert into GenulCartii(Gen, Descriere, NrCartiCuAcelasiGen) values ('Fictiune', 'Descriere3', '65')
insert into GenulCartii(Gen, Descriere, NrCartiCuAcelasiGen) values ('Mister', 'Descriere4', '34')
insert into GenulCartii(Gen, Descriere, NrCartiCuAcelasiGen) values ('Istoric', 'Descriere5', '21')
select * from GenulCartii
delete from GenulCartii where Gen = 'Mister'

insert into Carti(Titlu, Autor, NrSerie, NrPagini, IdGen) values ('Fata din tren', 'Paula Hawkins', 15475, 256, 1)
insert into Carti(Titlu, Autor, NrSerie, NrPagini, IdGen) values ('Ugly Love', 'Colleen Hoover', 25175, 300, 2)
insert into Carti(Titlu, Autor, NrSerie, NrPagini, IdGen) values ('Fluturi', 'Irina Binder', 22234, 160, 3)
insert into Carti(Titlu, Autor, NrSerie, NrPagini, IdGen) values ('Hotul de carti', 'Markus Zusak', 11278, 270, 4)
insert into Carti(Titlu, Autor, NrSerie, NrPagini, IdGen) values ('Secretele succesului', 'Dale Carnegie', 13368, 216, 5)


insert into Biblioteci(AdresaB, OrarBiblioteca, NrCititori) values ('Centru', '07:00 - 20:30', 56)
insert into Biblioteci(AdresaB, OrarBiblioteca, NrCititori) values ('Iris', '09:30 - 21:00', 25)
insert into Biblioteci(AdresaB, OrarBiblioteca, NrCititori) values ('Marasti', '10:00- 22:00', 214)
insert into Biblioteci(AdresaB, OrarBiblioteca, NrCititori) values ('Manastur', '08:30 - 20:30', 168)
insert into Biblioteci(AdresaB, OrarBiblioteca, NrCititori) values ('Gheorgheni', '09:30 - 21:00', 132)

insert into CartiBiblioteci(IdBiblioteca, IdCarte) values (1, 4), (1, 5), (2, 3), (4, 5), (3, 1)

insert into AdministratorBiblioteca(IdAdmin, Nume, Prenume, Experienta) values (1, 'Pop', 'Maria', 15)
insert into AdministratorBiblioteca(IdAdmin, Nume, Prenume, Experienta) values (2, 'Suciu', 'Denisa', 20)
insert into AdministratorBiblioteca(IdAdmin, Nume, Prenume, Experienta) values (3, 'Muresan', 'Ovidiu', 9)
insert into AdministratorBiblioteca(IdAdmin, Nume, Prenume, Experienta) values (4, 'Popescu', 'Andrei', 11)
insert into AdministratorBiblioteca(IdAdmin, Nume, Prenume, Experienta) values (5, 'Rusu', 'Alexandra', 5)

insert into Cititori(Nume, Prenume, Varsta, NrDeTelefon, Email) values ('Rusu', 'Flavia', 19, 0754781474, 'rusu@mail.com'), 
('Stan', 'Miruna', 15, 0721658984, 'stan@mail.com'), ('Matei', 'Raluca', 10, 0736547256, 'matei@mail.com'), 
('Cristea', 'Andrei', 21, 0747986451, 'cristea@mail.com'),
('Popa', 'Cristian', 25, 0754899657, 'popa@mail.com')

insert into CartiImprumutate(DataImprumut, DataReturn, nrCopiiDeCarti) values ('20.03', '01.04', 12)
insert into CartiImprumutate(DataImprumut, DataReturn, nrCopiiDeCarti) values ('15.05', '19.05', 10), ('02.10', '22.10', 3), 
('16.07', '08.08', 5), ('29.01', '07.02', 16)

insert into CategoriiDeVarsta(IdCategoriiDeVarsta,IntervalVarsta) values (1, 'Copii: 5-12 ani'), (2, 'Adolescenti: 14-18 ani'), 
(3, 'Studenti: 18-22'), (4, 'Adulti: 24-50 ani')

insert into CititoriCartiImprumutate(IdCarteImprum, IdCititor, NrCartiImprumutate) values (1, 2, 4), (2, 5, 2), (3, 4, 3), 
(4, 5, 2), (5, 1, 1), (2, 4, 4), (3, 1, 2), (1, 5, 7), (1, 3, 8), (4, 2, 6)

insert into CititoriCategoriiDeVarsta(IdCategoriiDeVarsta, IdCititor) values (1, 1)
insert into CititoriCategoriiDeVarsta(IdCategoriiDeVarsta, IdCititor) values (1, 5), (2, 4), (2, 3), (3, 1), (4, 2)

insert into GenulCartiiImprumutate(IdCarteImprum, IdGen) values (1, 4), (2, 2), (3, 2), (4, 1), (5, 3)


select * from GenulCartii
select * from Carti
select * from Biblioteci
select * from CartiBiblioteci
select * from AdministratorBiblioteca
select * from Cititori
select * from CartiImprumutate
select * from CategoriiDeVarsta
select * from CititoriCartiImprumutate
select * from CititoriCategoriiDeVarsta
select * from GenulCartiiImprumutate


-- (1) afiseaza cartile cu titlul dat si numarul de pagini mai mic decat 220
select IdCarte, Titlu, Autor, NrPagini
from Carti
where Titlu = 'Fluturi' or NrPagini < 220

-- (2) afiseaza adresele bibliotecilor care au nrCititori_maxim > 70
select AdresaB, max(NrCititori) as NrCititori_maxim
from Biblioteci
where NrCititori > 70
group by AdresaB
having max(NrCititori) > 150

-- (3) afiseaza cartile cu titlul si nrMaxim de pagini ordonate crescator
select IdCarte, Titlu, max(NrPagini) as NrPagini_maxim
from Carti
group by IdCarte, Titlu
order by NrPagini_maxim

-- (4) afiseaza bibliotecile cu orarul diferit
select OrarBiblioteca
from Biblioteci

select distinct OrarBiblioteca 
from Biblioteci

-- inner join pentru relatia m-n Carti - CartiBiblioteci - Biblioteci
-- (5) afiseaza cartile a caror nr de pagini e > 250 si se gasesc la 
-- adresele bibliotecilor care sunt in lista data
select *
from Carti c inner join CartiBiblioteci cb on c.IdCarte = cb.IdCarte
inner join Biblioteci b on b.IdBiblioteca = cb.IdBiblioteca
where c.NrPagini > 250 or b.AdresaB in ('Manastur', 'Centru', 'Marasti')


-- inner join pentru relatia m-n Cititori - CititoriCategoriiDeVarsta - CategoriiDeVarsta
-- (6) afiseaza cititorii in ordine crescatoare (dupa id) a caror varsta este >= 19
select cititori.IdCititor, cititori.Prenume, cititori.Varsta, cv.IntervalVarsta
--select * 
from Cititori cititori inner join CititoriCategoriiDeVarsta cvarsta on cititori.IdCititor = cvarsta.IdCititor
inner join CategoriiDeVarsta cv on cv.IdCategoriiDeVarsta = cvarsta.IdCategoriiDeVarsta
where cititori.Varsta >= 19
order by cititori.IdCititor


-- (7) afiseaza id-ul cartilor care au genul specificat si le ordoneaza dupa nr. de carti cu acelasi gen
select c.IdCarte, c.Titlu, c.Autor, c.NrPagini, gc.Gen, gc.NrCartiCuAcelasiGen
from Carti c inner join CartiBiblioteci cb on c.IdCarte = cb.IdCarte
inner join GenulCartii gc on gc.IdGen = c.IdGen
where gc.Gen in ('Mister', 'Fictiune')
group by c.IdCarte, c.Titlu, c.Autor, c.NrPagini, gc.Gen, gc.NrCartiCuAcelasiGen
order by NrCartiCuAcelasiGen


-- (8) afiseaza genul cartilor care au fost imprumutate,
-- unde nr. de carti cu acelasi gen este > 35 si nr. de pagini a unei carti este < 260
select g.IdGen, g.Gen, c.Titlu, c.NrPagini
from GenulCartii g inner join Carti c on g.IdGen = c.IdGen
inner join GenulCartiiImprumutate gci on gci.IdGen= g.IdGen
where NrCartiCuAcelasiGen > 35
group by  g.IdGen,  g.Gen, c.Titlu, c.NrPagini
having c.NrPagini < 260


-- (9) afiseaza cititorii care au carti imprumutate, varsta > 10, si nr. de carti existente < 12
select c.IdCititor, c.Varsta, cci.IdCarteImprum, ci.nrCopiiDeCarti
from Cititori c inner join CititoriCartiImprumutate cci on c.IdCititor = cci.IdCititor
inner join CartiImprumutate ci on ci.IdCarteImprum = c.IdCititor
where c.varsta > 10
group by c.IdCititor, c.Varsta, cci.IdCarteImprum, ci.nrCopiiDeCarti
having nrCopiiDeCarti < 12

-- (10) afiseaza administratorii de biblioteca cu Experienta de peste 10 ani
-- si a caror Prenume incep cu litera A si e urmat de cel putin 2 caractere
-- se afiseaza ordonat descrescator dupa anii de experienta
select a.IdAdmin, a.Nume, a.Prenume, a.Experienta
from AdministratorBiblioteca a inner join Biblioteci b on a.IdAdmin = b.IdBiblioteca
where a.Experienta > 10 or Prenume  = 'A_%'
order by Experienta desc


create or alter view View2 as
select a.IdAdmin, a.Nume, a.Prenume, a.Experienta, b.IdBiblioteca, b.OrarBiblioteca
from AdministratorBiblioteca a inner join Biblioteci b on a.IdAdmin = b.IdBiblioteca
where a.Experienta > 10 or Prenume  = 'A_%' and b.OrarBiblioteca = '08:30 - 20:30';
go