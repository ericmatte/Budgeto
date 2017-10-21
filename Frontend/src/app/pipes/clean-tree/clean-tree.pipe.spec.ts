import { CleanTreePipe } from './clean-tree.pipe';
import { TreePipe } from "app/pipes/tree/tree.pipe";
let pipe: CleanTreePipe;
let treePipe: TreePipe;

let categories: any[] = [
  { 'parent_id': null, 'category_id': 1, 'name': 'Revenu', 'items': [1, 2, 3] },
  { 'parent_id': 1, 'category_id': 4, 'name': 'Salaire', 'items': [] },
  { 'parent_id': 1, 'category_id': 9, 'name': 'Divers', 'items': [] },
  { 'parent_id': 9, 'category_id': 12, 'name': 'Casino', 'items': [4, 5] },
  { 'parent_id': null, 'category_id': 2, 'name': 'Dépense', 'items': [] },
  { 'parent_id': 2, 'category_id': 20, 'name': 'Facture', 'items': [7, 8] },
  { 'parent_id': null, 'category_id': 3, 'name': 'Transfert', 'items': [] },
];

beforeEach(() => {
  pipe = new CleanTreePipe();
  treePipe = new TreePipe();
});
describe('CleanTreePipe', () => {
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should clean a tree of category', () => {
    var expectingOutput = [
      {
        "parent_id": null, "category_id": 1, "name": "Revenu", "items": [1, 2, 3], "children":
        [
          {
            "parent_id": 1, "category_id": 9, "name": "Divers", "items": [], "children": [
              { "parent_id": 9, "category_id": 12, "name": "Casino", "items": [4, 5], "children": [], "count": 2 }
            ], "count": 2
          }
        ], "count": 5
      },
      {
        "parent_id": null, "category_id": 2, "name": "Dépense", "items": [], "children": [
          { "parent_id": 2, "category_id": 20, "name": "Facture", "items": [7, 8], "children": [], "count": 2 }
        ], "count": 2
      },
      { "parent_id": null, "category_id": 3, "name": "Transfert", "items": [], "children": [], "count": 0 }];

    var tree = treePipe.transform(categories, 'category_id', 'parent_id');
    var output = pipe.transform(tree);
    expect(output).toEqual(expectingOutput);
  });
});
