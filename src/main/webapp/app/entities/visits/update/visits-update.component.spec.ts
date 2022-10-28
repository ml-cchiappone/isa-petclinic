import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VisitsFormService } from './visits-form.service';
import { VisitsService } from '../service/visits.service';
import { IVisits } from '../visits.model';
import { IPets } from 'app/entities/pets/pets.model';
import { PetsService } from 'app/entities/pets/service/pets.service';

import { VisitsUpdateComponent } from './visits-update.component';

describe('Visits Management Update Component', () => {
  let comp: VisitsUpdateComponent;
  let fixture: ComponentFixture<VisitsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let visitsFormService: VisitsFormService;
  let visitsService: VisitsService;
  let petsService: PetsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VisitsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VisitsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VisitsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    visitsFormService = TestBed.inject(VisitsFormService);
    visitsService = TestBed.inject(VisitsService);
    petsService = TestBed.inject(PetsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pets query and add missing value', () => {
      const visits: IVisits = { id: 456 };
      const pet: IPets = { id: 45511 };
      visits.pet = pet;

      const petsCollection: IPets[] = [{ id: 24170 }];
      jest.spyOn(petsService, 'query').mockReturnValue(of(new HttpResponse({ body: petsCollection })));
      const additionalPets = [pet];
      const expectedCollection: IPets[] = [...additionalPets, ...petsCollection];
      jest.spyOn(petsService, 'addPetsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ visits });
      comp.ngOnInit();

      expect(petsService.query).toHaveBeenCalled();
      expect(petsService.addPetsToCollectionIfMissing).toHaveBeenCalledWith(petsCollection, ...additionalPets.map(expect.objectContaining));
      expect(comp.petsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const visits: IVisits = { id: 456 };
      const pet: IPets = { id: 95893 };
      visits.pet = pet;

      activatedRoute.data = of({ visits });
      comp.ngOnInit();

      expect(comp.petsSharedCollection).toContain(pet);
      expect(comp.visits).toEqual(visits);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVisits>>();
      const visits = { id: 123 };
      jest.spyOn(visitsFormService, 'getVisits').mockReturnValue(visits);
      jest.spyOn(visitsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visits });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: visits }));
      saveSubject.complete();

      // THEN
      expect(visitsFormService.getVisits).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(visitsService.update).toHaveBeenCalledWith(expect.objectContaining(visits));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVisits>>();
      const visits = { id: 123 };
      jest.spyOn(visitsFormService, 'getVisits').mockReturnValue({ id: null });
      jest.spyOn(visitsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visits: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: visits }));
      saveSubject.complete();

      // THEN
      expect(visitsFormService.getVisits).toHaveBeenCalled();
      expect(visitsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVisits>>();
      const visits = { id: 123 };
      jest.spyOn(visitsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visits });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(visitsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePets', () => {
      it('Should forward to petsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(petsService, 'comparePets');
        comp.comparePets(entity, entity2);
        expect(petsService.comparePets).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
