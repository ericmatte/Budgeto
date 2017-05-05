import codecs
import json

from lib.rsa import RSA
from lib.statement_parser import StatementParser

"""
Desjardins CSV format:
https://www.desjardins.com/en/services_en_ligne/accesd_affaires/soutien/guiderel.pdf
"""

path = 'tests/resources/csv/'
csv_files = ['Desjardins_Comptes.csv', 'Tangerine_Credit-Card.CSV', 'Tangerine_Saving-Account.CSV']
json_files = ['Desjardins_Comptes.json', 'Tangerine_Credit-Card.json', 'Tangerine_Saving-Account.json']
accounts = [
    {'bank': 'Desjardins', 'csv': 'Desjardins_Comptes.csv', 'json': 'Desjardins_Comptes.json'}
]

def test_csv_to_json(client):
    for i in range(1):
        statement = accounts[i]

        parsed = StatementParser.parse(path+statement['csv'], statement['bank'])
        with codecs.open(path+statement['json'], 'r', encoding='utf8') as f:
            ref = json.loads(f.read())

            assert parsed['bank'] == ref['bank']
            for i in range(len(parsed['transactions'])):
                assert parsed['transactions'][i]['description'] == ref['transactions'][i]['description']
                assert parsed['transactions'][i]['date']        == ref['transactions'][i]['date']
                assert parsed['transactions'][i]['amount']      == ref['transactions'][i]['amount']

def test_clean_text(client):
    text = StatementParser.clean_text('TPS /CANADA Un test accesD')
    assert text == 'Tps / Canada Un test accesD'
