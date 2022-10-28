import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHuman } from '../human.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../human.test-samples';

import { HumanService } from './human.service';

const requireRestSample: IHuman = {
  ...sampleWithRequiredData,
};

describe('Human Service', () => {
  let service: HumanService;
  let httpMock: HttpTestingController;
  let expectedResult: IHuman | IHuman[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HumanService);
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

    it('should create a Human', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const human = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(human).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Human', () => {
      const human = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(human).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Human', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Human', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Human', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHumanToCollectionIfMissing', () => {
      it('should add a Human to an empty array', () => {
        const human: IHuman = sampleWithRequiredData;
        expectedResult = service.addHumanToCollectionIfMissing([], human);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(human);
      });

      it('should not add a Human to an array that contains it', () => {
        const human: IHuman = sampleWithRequiredData;
        const humanCollection: IHuman[] = [
          {
            ...human,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHumanToCollectionIfMissing(humanCollection, human);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Human to an array that doesn't contain it", () => {
        const human: IHuman = sampleWithRequiredData;
        const humanCollection: IHuman[] = [sampleWithPartialData];
        expectedResult = service.addHumanToCollectionIfMissing(humanCollection, human);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(human);
      });

      it('should add only unique Human to an array', () => {
        const humanArray: IHuman[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const humanCollection: IHuman[] = [sampleWithRequiredData];
        expectedResult = service.addHumanToCollectionIfMissing(humanCollection, ...humanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const human: IHuman = sampleWithRequiredData;
        const human2: IHuman = sampleWithPartialData;
        expectedResult = service.addHumanToCollectionIfMissing([], human, human2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(human);
        expect(expectedResult).toContain(human2);
      });

      it('should accept null and undefined values', () => {
        const human: IHuman = sampleWithRequiredData;
        expectedResult = service.addHumanToCollectionIfMissing([], null, human, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(human);
      });

      it('should return initial array if no Human is added', () => {
        const humanCollection: IHuman[] = [sampleWithRequiredData];
        expectedResult = service.addHumanToCollectionIfMissing(humanCollection, undefined, null);
        expect(expectedResult).toEqual(humanCollection);
      });
    });

    describe('compareHuman', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHuman(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHuman(entity1, entity2);
        const compareResult2 = service.compareHuman(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHuman(entity1, entity2);
        const compareResult2 = service.compareHuman(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHuman(entity1, entity2);
        const compareResult2 = service.compareHuman(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
