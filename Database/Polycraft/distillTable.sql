PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE distillrecipe (
input1 text PRIMARY KEY,
inQuant1 integer,
output1 text NOT NULL,
outQuant1 integer,
output2 text,
outQuant2 integer
,
output3 text,
outQuant3 integer,
output4 text,
outQuant4 integer,
output5 text,
outQuant5 integer,
output6 text,
outQuant6 integer,
output7 text,
outQuant7 integer,
output8 text,
outQuant8 integer,
output9 text,
outQuant9 integer
);
INSERT INTO "distillrecipe" VALUES('Drum (Crude Oil)',64,'Drum (Naphthalene)',21,'Drum (Benzene-Toluene-Xylene)',21,'Drum (Gas Oil)',21,'Block of Bitumen',28,'Vanadium Pentoxide Catalyst',4,'','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (NeoPentane)',64,'Canister (Methane)',64,'Canister (Methane)',40,'Canister (Ethane)',8,'Canister (Propane)',4,'Cartridge (Butane Isomers)',2,'Vial (Pentane Isomers)',20,'Vial (Hexane Isomers)',2,NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Naphthalene)',64,'Drum (Light Naphtha)',32,'Drum (Heavy Naphtha)',32,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Light Naphtha)',64,'Drum (Light Parrafins)',20,'Drum (Light Naphthenes)',20,'Drum (Light Olefins)',24,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Light Parrafins)',64,'Canister (Methane)',52,'Canister (Ethane)',4,'Canister (Propane)',2,'Canister (Butane Isomers)',1,'Vial (Pentane Isomers)',10,'Vial (Hexane Isomers)',1,'','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Canister (Butane Isomers)',64,'Drum (n-Butane)',42,'Drum (IsoButane)',21,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Pentane Isomers)',64,'Drum (Naphtha)',23,'Drum (IsoPentane)',40,'Canister (Nitrogen)',1,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Hexane Isomers)',64,'Drum (n-Pentane)',17,'Drum (2-MethylPentane)',20,'Drum (3-MethylPentane)',11,'Drum (2,3-DiMethylButane)',12,'Drum (2,2-DiMethylButane)',3,'','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Apple',64,'Beaker (Fruit Brandy)',1,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Potato',64,'Beaker (Vodka)',1,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Nether Wart',64,'Beaker (Gin)',1,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Cactus',64,'Beaker (Tequila)',1,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Reeds',64,'Beaker (Rum)',4,'Beaker (Glycolic Acid)',4,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Wheat',64,'Beaker (Whiskey)',1,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Carrot',64,'Beaker (Carrot Wine)',1,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Naphtha)',64,'Vial (Light Naphtha)',64,'Vial (Heavy Naphtha)',64,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Naphtha)',64,'Beaker (Light Naphtha)',64,'Beaker (Heavy Naphtha)',64,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Naphtha)',64,'Drum (Light Naphtha)',64,'Drum (Heavy Naphtha)',64,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Light Olefins)',64,'Canister (Propylene)',32,'Canister (Ethylene)',32,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Benzene-Toluene-Xylene)',64,'Drum (Benzene)',64,'Drum (Toluene)',64,'Drum (Xylene)',64,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Leaves',64,'Vial (Isoprene)',64,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Xylene)',63,'Drum (o-Xylene)',21,'Drum (p-Xylene)',21,'Drum (m-Xylene)',21,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Xylene)',48,'Vial (o-Xylene)',16,'Vial (p-Xylene)',16,'Vial (m-Xylene)',16,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Xylene)',48,'Beaker (o-Xylene)',16,'Beaker (p-Xylene)',16,'Beaker (m-Xylene)',16,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Light Olefins)',32,'Flask (Propylene)',16,'Flask (Ethylene)',16,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Light Olefins)',32,'Cartridge (Propylene)',16,'Cartridge (Ethylene)',16,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Gas Oil)',32,'Vial (Kerosene)',16,'Vial (Diesel)',16,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Drum (Gas Oil)',32,'Drum (Kerosene)',16,'Drum (Diesel)',16,'Bitumen',64,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Gas Oil)',27,'Beaker (Kerosene)',9,'Beaker (Diesel)',9,'Bitumen',9,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Crude Oil)',16,'Vial (Naphthalene)',12,'Vial (Benzene-Toluene-Xylene)',2,'Vial (Gas Oil)',2,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Crude Oil)',16,'Beaker (Naphthalene)',8,'Beaker (Benzene-Toluene-Xylene)',3,'Beaker (Gas Oil)',3,'Bitumen',2,'','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (NeoPentane)',16,'Flask (Methane)',32,'','','','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (NeoPentane)',16,'Cartridge (Methane)',26,'Cartridge (Ethane)',2,'Cartridge (Propane)',1,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Naphthalene)',16,'Vial (Light Naphtha)',12,'Vial (Heavy Naphtha)',4,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Naphthalene)',16,'Beaker (Light Naphtha)',12,'Beaker (Heavy Naphtha)',4,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Light Naphtha)',16,'Vial (Light Parrafins)',2,'Vial (Light Naphthenes)',2,'Vial (Light Olefins)',12,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Light Naphtha)',16,'Beaker (Light Parrafins)',3,'Beaker (Light Naphthenes)',3,'Beaker (Light Olefins)',10,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Light Parrafins)',16,'Flask (Methane)',14,'Flask (Ethane)',2,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Light Parrafins)',16,'Cartridge (Methane)',12,'Cartridge (Ethane)',2,'Flask (Propane)',16,'Flask (Butane Isomers)',16,'','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Flask (Butane Isomers)',16,'Vial (n-Butane)',10,'Vial (IsoButane)',5,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Cartridge (Butane Isomers)',16,'Beaker (n-Butane)',10,'Beaker (IsoButane)',5,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Pentane Isomers)',16,'Vial (Naphtha)',6,'Vial (IsoPentane)',10,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Pentane Isomers)',16,'Beaker (Naphtha)',6,'Beaker (IsoPentane)',10,'Flask (Nitrogen)',12,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Hexane Isomers)',16,'Vial (n-Pentane)',5,'Vial (2-MethylPentane)',6,'Vial (3-MethylPentane)',3,'Vial (2,3-DiMethylButane)',3,'','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Hexane Isomers)',16,'Beaker (n-Pentane)',5,'Beaker (2-MethylPentane)',6,'Beaker (3-MethylPentane)',3,'Beaker (2,3-DiMethylButane)',3,'Vial (2,2-DiMethylButane)',48,'','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Vial (Benzene-Toluene-Xylene)',16,'Vial (Benzene)',16,'Vial (Toluene)',16,'Vial (Xylene)',16,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Beaker (Benzene-Toluene-Xylene)',16,'Beaker (Benzene)',16,'Beaker (Toluene)',16,'Beaker (Xylene)',16,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Tar Sand',4,'Bitumen',3,'Sand',1,'Bag (Anthracene)',4,'','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Shale',4,'Beaker (NeoPentane)',3,'Gravel',1,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Bitumen',4,'Beaker (Crude Oil)',1,'Gravel',3,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Block of Bitumen',4,'Beaker (Crude Oil)',11,'Gravel',32,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Water Bucket',1,'Beaker (Salt Water)',1,'Bucket',1,'','','','','','','','','','',NULL,NULL,NULL,NULL);
INSERT INTO "distillrecipe" VALUES('Bucket (Crude Oil)',1,'Beaker (Crude Oil)',1,'Bucket',1,'','','','','','','','','',NULL,NULL,NULL,NULL,NULL);
COMMIT;
