import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PetsFormService } from './pets-form.service';
import { PetsService } from '../service/pets.service';
import { IPets } from '../pets.model';
import { ITypes } from 'app/entities/types/types.model';
import { TypesService } from 'app/entities/types/service/types.service';
import { IHuman } from 'app/entities/human/human.model';
import { HumanService } from 'app/entities/human/service/human.service';
import { IVet } from 'app/entities/vet/vet.model';
import { VetService } from 'app/entities/vet/service/vet.service';

import { PetsUpdateComponent } from './pets-update.component';

describe('Pets Management Update Component', () => {
  let comp: PetsUpdateComponent;
  let fixture: ComponentFixture<PetsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let petsFormService: PetsFormService;
  let petsService: PetsService;
  let typesService: TypesService;
  let humanService: HumanService;
  let vetService: VetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PetsUpdateComponent],
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
      .overrideTemplate(PetsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PetsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    petsFormService = TestBed.inject(PetsFormService);
    petsService = TestBed.inject(PetsService);
    typesService = TestBed.inject(TypesService);
    humanService = TestBed.inject(HumanService);
    vetService = TestBed.inject(VetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Types query and add missing value', () => {
      const pets: IPets = { id: 456 };
      const type: ITypes = { id: 68268 };
      pets.type = type;

      const typesCollection: ITypes[] = [{ id: 24837 }];
      jest.spyOn(typesService, 'query').mockReturnValue(of(new HttpResponse({ body: typesCollection })));
      const additionalTypes = [type];
      const expectedCollection: ITypes[] = [...additionalTypes, ...typesCollection];
      jest.spyOn(typesService, 'addTypesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pets });
      comp.ngOnInit();

      expect(typesService.query).toHaveBeenCalled();
      expect(typesService.addTypesToCollectionIfMissing).toHaveBeenCalledWith(
        typesCollection,
        ...additionalTypes.map(expect.objectContaining)
      );
      expect(comp.typesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Human query and add missing value', () => {
      const pets: IPets = { id: 456 };
      const human: IHuman = { id: 48684 };
      pets.human = human;

      const humanCollection: IHuman[] = [{ id: 42225 }];
      jest.spyOn(humanService, 'query').mockReturnValue(of(new HttpResponse({ body: humanCollection })));
      const additionalHumans = [human];
      const expectedCollection: IHuman[] = [...additionalHumans, ...humanCollection];
      jest.spyOn(humanService, 'addHumanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pets });
      comp.ngOnInit();

      expect(humanService.query).toHaveBeenCalled();
      expect(humanService.addHumanToCollectionIfMissing).toHaveBeenCalledWith(
        humanCollection,
        ...additionalHumans.map(expect.objectContaining)
      );
      expect(comp.humansSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vet query and add missing value', () => {
      const pets: IPets = { id: 456 };
      const vet: IVet = { id: 49812 };
      pets.vet = vet;

      const vetCollection: IVet[] = [{ id: 85304 }];
      jest.spyOn(vetService, 'query').mockReturnValue(of(new HttpResponse({ body: vetCollection })));
      const additionalVets = [vet];
      const expectedCollection: IVet[] = [...additionalVets, ...vetCollection];
      jest.spyOn(vetService, 'addVetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pets });
      comp.ngOnInit();

      expect(vetService.query).toHaveBeenCalled();
      expect(vetService.addVetToCollectionIfMissing).toHaveBeenCalledWith(vetCollection, ...additionalVets.map(expect.objectContaining));
      expect(comp.vetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pets: IPets = { id: 456 };
      const type: ITypes = { id: 24684 };
      pets.type = type;
      const human: IHuman = { id: 96034 };
      pets.human = human;
      const vet: IVet = { id: 40673 };
      pets.vet = vet;

      activatedRoute.data = of({ pets });
      comp.ngOnInit();

      expect(comp.typesSharedCollection).toContain(type);
      expect(comp.humansSharedCollection).toContain(human);
      expect(comp.vetsSharedCollection).toContain(vet);
      expect(comp.pets).toEqual(pets);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPets>>();
      const pets = { id: 123 };
      jest.spyOn(petsFormService, 'getPets').mockReturnValue(pets);
      jest.spyOn(petsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pets }));
      saveSubject.complete();

      // THEN
      expect(petsFormService.getPets).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(petsService.update).toHaveBeenCalledWith(expect.objectContaining(pets));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPets>>();
      const pets = { id: 123 };
      jest.spyOn(petsFormService, 'getPets').mockReturnValue({ id: null });
      jest.spyOn(petsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pets: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pets }));
      saveSubject.complete();

      // THEN
      expect(petsFormService.getPets).toHaveBeenCalled();
      expect(petsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPets>>();
      const pets = { id: 123 };
      jest.spyOn(petsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(petsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypes', () => {
      it('Should forward to typesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typesService, 'compareTypes');
        comp.compareTypes(entity, entity2);
        expect(typesService.compareTypes).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareHuman', () => {
      it('Should forward to humanService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(humanService, 'compareHuman');
        comp.compareHuman(entity, entity2);
        expect(humanService.compareHuman).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVet', () => {
      it('Should forward to vetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vetService, 'compareVet');
        comp.compareVet(entity, entity2);
        expect(vetService.compareVet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
