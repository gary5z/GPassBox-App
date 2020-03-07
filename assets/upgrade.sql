ALTER TABLE System ADD Version INTEGER;
update System set Version=2;

ALTER TABLE Category ADD CatType INTEGER;
update Category set CatType=1 where CatId=1;
update Category set CatType=2 where CatId=2;
update Category set CatType=3 where CatId=3;
update Category set CatType=4 where CatId=1;
