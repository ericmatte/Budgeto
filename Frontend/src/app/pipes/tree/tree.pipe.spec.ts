import { TreePipe } from './tree.pipe';
let pipe: TreePipe;

let categories: any[] = [
  { 'parent_id': null, 'category_id': 1, 'name': 'Revenu' },
  { 'parent_id': 1, 'category_id': 4, 'name': 'Salaire' },
  { 'parent_id': 1, 'category_id': 9, 'name': 'Divers' },
  { 'parent_id': 9, 'category_id': 12, 'name': 'Casino' },
  { 'parent_id': null, 'category_id': 2, 'name': 'Dépense' },
  { 'parent_id': 2, 'category_id': 20, 'name': 'Facture' },
  { 'parent_id': null, 'category_id': 3, 'name': 'Transfert' },
];

beforeEach(() => pipe = new TreePipe());
describe('TreePipe', () => {
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should create a tree of category', () => {
    var expectingOutput = [
      {
        'parent_id': null, 'category_id': 1, 'name': 'Revenu', 'children': [
          { 'parent_id': 1, 'category_id': 4, 'name': 'Salaire', 'children': [] },
          { 'parent_id': 1, 'category_id': 9, 'name': 'Divers', 'children': [{ 'parent_id': 9, 'category_id': 12, 'name': 'Casino', 'children': [] }] },
        ]
      },
      {
        'parent_id': null, 'category_id': 2, 'name': 'Dépense', 'children': [
          { 'parent_id': 2, 'category_id': 20, 'name': 'Facture', 'children': [] },
        ]
      },
      { 'parent_id': null, 'category_id': 3, 'name': 'Transfert', 'children': [] },
    ];

    var output = pipe.transform(categories, 'category_id', 'parent_id');
    expect(output).toEqual(expectingOutput);
  });
});
