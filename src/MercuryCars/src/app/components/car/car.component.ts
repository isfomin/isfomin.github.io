import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-car',
  templateUrl: './car.component.html',
  styleUrls: ['./car.component.sass']
})
export class CarComponent implements OnInit {

  name:string;
  speed:number;
  model:string;
  colors:Colors;
  options:string[];
  test:any;

  constructor() { }

  ngOnInit() {
    this.name = "Honda";
    this.speed = 245;
    this.model = "Civic";
    this.colors = {
      car: "Серый",
      salon: "Чёрный",
      wheels: "Серебристый"
    };
    this.options = ["ABS", "Автопилот", "Авто Паркинг"];
    this.test = "test";
  }

  carSelect(carName) {
    if (carName == 'bmw') {
      this.name = "BMW";
      this.speed = 280;
      this.model = "640i";
      this.colors = {
        car: "Зелёный",
        salon: "Чёрный",
        wheels: "Черный"
      };
      this.options = ["ABS", "Круизконтроль"];
      this.test = "test";
    } else if (carName == 'honda') {
      this.name = "Honda";
      this.speed = 245;
      this.model = "Civic";
      this.colors = {
        car: "Серый",
        salon: "Чёрный",
        wheels: "Серебристый"
      };
      this.options = ["ABS", "Автопилот", "Авто Паркинг"];
      this.test = "test";
    } else if (carName == 'merc') {
      this.name = "Mercedes";
      this.speed = 320;
      this.model = "E63S 4MATIC+";
      this.colors = {
        car: "Серый",
        salon: "Чёрный",
        wheels: "Серебристый"
      };
      this.options = ["ABS"];
      this.test = "test";
    }
  }
}

interface Colors {
  car:string,
  salon:string,
  wheels:string
}
