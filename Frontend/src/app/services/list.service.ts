import { Injectable } from '@angular/core';

@Injectable()
export class ListService {
  constructor() { }

  public add(list: any[], item: any) {
    list.push(item);
    return list.slice(0);
  }

  public copy(list: any[]) {
    return list.slice(0);
  }

  public replace(list: any[], currentItem: any, newItem: any) {
    const i = list.indexOf(currentItem);
    if (i == -1) { return list; }
    list[i] = newItem;
    return list.slice(0);
  }

  public remove(list: any[], item: any) {
    const i = list.indexOf(item);
    if (i == -1) { return list; }
    list.splice(i, 1);
    return list.slice(0);
  }
}
