drop table ECRIRE;
drop table AUTEUR;
drop table DETAILCOMMANDE;
drop table COMMANDE;
drop table POSSEDER;
drop table LIVRE;
drop table VENDEUR;
drop table MAGASIN;
drop table CLIENT;


CREATE TABLE AUTEUR (
  PRIMARY KEY (idauteur),
  idauteur   varchar(11) NOT NULL,
  nomauteur  varchar(100),
  anneenais  int,
  anneedeces int
);

CREATE TABLE CLIENT (
  PRIMARY KEY (idcli),
  idcli      int NOT NULL,
  nomcli     varchar(50),
  prenomcli  varchar(30),
  motdepassecli varchar(30) UNIQUE,
  adressecli varchar(100),
  codepostal varchar(5),
  villecli   varchar(100)
);

CREATE TABLE COMMANDE (
  PRIMARY KEY (numcom),
  numcom  int NOT NULL,
  datecom date,
  enligne char(1),
  livraison char(1),
  idcli   int NOT NULL,
  idmag   int NOT NULL
);

CREATE TABLE DETAILCOMMANDE (
  PRIMARY KEY (numcom, numlig),
  numcom    int NOT NULL,
  numlig    int NOT NULL,
  qte       int,
  prixvente decimal(6,2),
  isbn      varchar(13) NOT NULL
);

CREATE TABLE ECRIRE (
  PRIMARY KEY (isbn, idauteur),
  isbn     varchar(13) NOT NULL,
  idauteur varchar(11) NOT NULL
);

CREATE TABLE LIVRE (
  PRIMARY KEY (isbn),
  isbn      varchar(13) NOT NULL,
  titre     varchar(200),
  nbpages   int,
  datepubli int,
  prix      decimal(6,2),
  nomclass  varchar(50),
  nomedit   varchar(100),
  img       MEDIUMBLOB

);

CREATE TABLE MAGASIN (
  PRIMARY KEY (idmag),
  idmag    int NOT NULL,
  nommag   VARCHAR(42),
  villemag VARCHAR(42)
);

CREATE TABLE POSSEDER (
  PRIMARY KEY (idmag, isbn),
  idmag int NOT NULL,
  isbn  varchar(13) NOT NULL,
  qte   int NOT NULL
);

CREATE TABLE VENDEUR (
  PRIMARY KEY (numVendeur),
  numVendeur int NOT NULL,
  nomV       VarChar(30),
  prenomV    VarChar(30),
  adresseV   VarChar(100),
  codepostalV varchar(5),
  villeV     VarChar(100),
  idmag      int NOT NULL
);

ALTER TABLE COMMANDE ADD FOREIGN KEY (idmag) REFERENCES MAGASIN (idmag);
ALTER TABLE COMMANDE ADD FOREIGN KEY (idcli) REFERENCES CLIENT (idcli);

ALTER TABLE DETAILCOMMANDE ADD FOREIGN KEY (isbn) REFERENCES LIVRE (isbn);
ALTER TABLE DETAILCOMMANDE ADD FOREIGN KEY (numcom) REFERENCES COMMANDE (numcom);

ALTER TABLE ECRIRE ADD FOREIGN KEY (idauteur) REFERENCES AUTEUR (idauteur);
ALTER TABLE ECRIRE ADD FOREIGN KEY (isbn) REFERENCES LIVRE (isbn);

ALTER TABLE POSSEDER ADD FOREIGN KEY (isbn) REFERENCES LIVRE (isbn);
ALTER TABLE POSSEDER ADD FOREIGN KEY (idmag) REFERENCES MAGASIN (idmag);

ALTER TABLE VENDEUR ADD FOREIGN KEY (idmag) REFERENCES MAGASIN (idmag);