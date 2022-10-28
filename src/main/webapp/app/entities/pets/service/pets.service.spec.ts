import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPets } from '../pets.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pets.test-samples';

import { PetsService } from './pets.service';

const requireRestSample: IPets = {
  ...sampleWithRequiredData,
};

describe('Pets Service', () => {
  let service: PetsService;
  let httpMock: HttpTestingController;
  let expectedResult: IPets | IPets[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PetsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Pets', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pets = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pets).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pets', () => {
      const pets = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pets).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pets', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pets', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pets', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPetsToCollectionIfMissing', () => {
      it('should add a Pets to an empty array', () => {
        const pets: IPets = sampleWithRequiredData;
        expectedResult = service.addPetsToCollectionIfMissing([], pets);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pets);
      });

      it('should not add a Pets to an array that contains it', () => {
        const pets: IPets = sampleWithRequiredData;
        const petsCollection: IPets[] = [
          {
            ...pets,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPetsToCollectionIfMissing(petsCollection, pets);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pets to an array that doesn't contain it", () => {
        const pets: IPets = sampleWithRequiredData;
        const petsCollection: IPets[] = [sampleWithPartialData];
        expectedResult = service.addPetsToCollectionIfMissing(petsCollection, pets);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pets);
      });

      it('should add only unique Pets to an array', () => {
        const petsArray: IPets[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const petsCollection: IPets[] = [sampleWithRequiredData];
        expectedResult = service.addPetsToCollectionIfMissing(petsCollection, ...petsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pets: IPets = sampleWithRequiredData;
        const pets2: IPets = sampleWithPartialData;
        expectedResult = service.addPetsToCollectionIfMissing([], pets, pets2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pets);
        expect(expectedResult).toContain(pets2);
      });

      it('should accept null and undefined values', () => {
        const pets: IPets = sampleWithRequiredData;
        expectedResult = service.addPetsToCollectionIfMissing([], null, pets, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pets);
      });

      it('should return initial array if no Pets is added', () => {
        const petsCollection: IPets[] = [sampleWithRequiredData];
        expectedResult = service.addPetsToCollectionIfMissing(petsCollection, undefined, null);
        expect(expectedResult).toEqual(petsCollection);
      });
    });

    describe('comparePets', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePets(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePets(entity1, entity2);
        const compareResult2 = service.comparePets(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePets(entity1, entity2);
        const compareResult2 = service.comparePets(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePets(entity1, entity2);
        const compareResult2 = service.comparePets(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
