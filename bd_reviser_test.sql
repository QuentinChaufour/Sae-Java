drop table testECRIRE;
drop table testAUTEUR;
drop table testDETAILCOMMANDE;
drop table testCOMMANDE;
drop table testPOSSEDER;
drop table testLIVRE;
drop table testVENDEUR;
drop table testMAGASIN;
drop table testCLIENT;


CREATE TABLE testAUTEUR (
  PRIMARY KEY (idauteur),
  idauteur   varchar(11) NOT NULL,
  nomauteur  varchar(100),
  anneenais  int,
  anneedeces int
);

CREATE TABLE testCLIENT (
  PRIMARY KEY (idcli),
  idcli      int NOT NULL,
  nomcli     varchar(50),
  prenomcli  varchar(30),
  motdepassecli varchar(30) UNIQUE,
  adressecli varchar(100),
  codepostal varchar(5),
  villecli   varchar(100)
);

CREATE TABLE testCOMMANDE (
  PRIMARY KEY (numcom),
  numcom  int NOT NULL,
  datecom date,
  enligne char(1),
  livraison char(1),
  idcli   int NOT NULL,
  idmag   int NOT NULL
);

CREATE TABLE testDETAILCOMMANDE (
  PRIMARY KEY (numcom, numlig),
  numcom    int NOT NULL,
  numlig    int NOT NULL,
  qte       int,
  prixvente decimal(6,2),
  isbn      varchar(13) NOT NULL
);

CREATE TABLE testECRIRE (
  PRIMARY KEY (isbn, idauteur),
  isbn     varchar(13) NOT NULL,
  idauteur varchar(11) NOT NULL
);

CREATE TABLE testLIVRE (
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

CREATE TABLE testMAGASIN (
  PRIMARY KEY (idmag),
  idmag    int NOT NULL,
  nommag   VARCHAR(42),
  villemag VARCHAR(42)
);

CREATE TABLE testPOSSEDER (
  PRIMARY KEY (idmag, isbn),
  idmag int NOT NULL,
  isbn  varchar(13) NOT NULL,
  qte   int NOT NULL
);

CREATE TABLE testVENDEUR (
  PRIMARY KEY (numVendeur),
  numVendeur int NOT NULL,
  nomV       VarChar(30),
  prenomV    VarChar(30),
  adresseV   VarChar(100),
  codepostalV varchar(5),
  villeV     VarChar(100),
  idmag      int NOT NULL
);

ALTER TABLE testCOMMANDE ADD FOREIGN KEY (idmag) REFERENCES testMAGASIN (idmag);
ALTER TABLE testCOMMANDE ADD FOREIGN KEY (idcli) REFERENCES testCLIENT (idcli);

ALTER TABLE testDETAILCOMMANDE ADD FOREIGN KEY (isbn) REFERENCES testLIVRE (isbn);
ALTER TABLE testDETAILCOMMANDE ADD FOREIGN KEY (numcom) REFERENCES testCOMMANDE (numcom);

ALTER TABLE testECRIRE ADD FOREIGN KEY (idauteur) REFERENCES testAUTEUR (idauteur);
ALTER TABLE testECRIRE ADD FOREIGN KEY (isbn) REFERENCES testLIVRE (isbn);

ALTER TABLE testPOSSEDER ADD FOREIGN KEY (isbn) REFERENCES testLIVRE (isbn);
ALTER TABLE testPOSSEDER ADD FOREIGN KEY (idmag) REFERENCES testMAGASIN (idmag);

ALTER TABLE testVENDEUR ADD FOREIGN KEY (idmag) REFERENCES testMAGASIN (idmag);