import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVisits } from '../visits.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../visits.test-samples';

import { VisitsService, RestVisits } from './visits.service';

const requireRestSample: RestVisits = {
  ...sampleWithRequiredData,
  visitdate: sampleWithRequiredData.visitdate?.toJSON(),
};

describe('Visits Service', () => {
  let service: VisitsService;
  let httpMock: HttpTestingController;
  let expectedResult: IVisits | IVisits[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VisitsService);
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

    it('should create a Visits', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const visits = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(visits).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Visits', () => {
      const visits = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(visits).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Visits', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Visits', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Visits', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVisitsToCollectionIfMissing', () => {
      it('should add a Visits to an empty array', () => {
        const visits: IVisits = sampleWithRequiredData;
        expectedResult = service.addVisitsToCollectionIfMissing([], visits);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(visits);
      });

      it('should not add a Visits to an array that contains it', () => {
        const visits: IVisits = sampleWithRequiredData;
        const visitsCollection: IVisits[] = [
          {
            ...visits,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVisitsToCollectionIfMissing(visitsCollection, visits);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Visits to an array that doesn't contain it", () => {
        const visits: IVisits = sampleWithRequiredData;
        const visitsCollection: IVisits[] = [sampleWithPartialData];
        expectedResult = service.addVisitsToCollectionIfMissing(visitsCollection, visits);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(visits);
      });

      it('should add only unique Visits to an array', () => {
        const visitsArray: IVisits[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const visitsCollection: IVisits[] = [sampleWithRequiredData];
        expectedResult = service.addVisitsToCollectionIfMissing(visitsCollection, ...visitsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const visits: IVisits = sampleWithRequiredData;
        const visits2: IVisits = sampleWithPartialData;
        expectedResult = service.addVisitsToCollectionIfMissing([], visits, visits2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(visits);
        expect(expectedResult).toContain(visits2);
      });

      it('should accept null and undefined values', () => {
        const visits: IVisits = sampleWithRequiredData;
        expectedResult = service.addVisitsToCollectionIfMissing([], null, visits, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(visits);
      });

      it('should return initial array if no Visits is added', () => {
        const visitsCollection: IVisits[] = [sampleWithRequiredData];
        expectedResult = service.addVisitsToCollectionIfMissing(visitsCollection, undefined, null);
        expect(expectedResult).toEqual(visitsCollection);
      });
    });

    describe('compareVisits', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVisits(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVisits(entity1, entity2);
        const compareResult2 = service.compareVisits(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVisits(entity1, entity2);
        const compareResult2 = service.compareVisits(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVisits(entity1, entity2);
        const compareResult2 = service.compareVisits(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
