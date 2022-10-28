import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuman } from '../human.model';

@Component({
  selector: 'jhi-human-detail',
  templateUrl: './human-detail.component.html',
})
export class HumanDetailComponent implements OnInit {
  human: IHuman | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ human }) => {
      this.human = human;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
