import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VetDetailComponent } from './vet-detail.component';

describe('Vet Management Detail Component', () => {
  let comp: VetDetailComponent;
  let fixture: ComponentFixture<VetDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VetDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vet: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VetDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VetDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vet on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
