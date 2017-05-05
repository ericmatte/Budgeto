import csv
import os
import re

from models import Bank


class StatementParser:

    @staticmethod
    def parse(file, bank_name):
        bank = Bank.get(name=bank_name.title())
        if bank is None:
            raise ValueError('Bank not supported!')

        extension = os.path.splitext(file)[1]
        if extension.lower() == '.csv':
            return StatementParser.__parse_csv(file, bank)
        else:
            raise ValueError('Filetype not supported!')

    @staticmethod
    def clean_text(text):
        text = re.sub('( *)\/( *)', ' / ', text) # Put uniform spaces around '/'
        text = re.sub(r'([A-Z])([A-Z]+)\b', lambda m: m.group(1)+m.group(2).lower(), text) # Put uniform spaces around '/'
        return text.strip()

    @staticmethod
    def __parse_csv(file, bank):
        with open(file, newline='') as csv_file:
            reader = csv.reader(csv_file, delimiter=',')
            rows = [[value.strip() for value in row] for row in reader]

            output = {'bank':bank.name, 'transactions':[]}

            if bank.name == 'Desjardins':
                for row in rows:
                    if len(row) == 14:
                        # 3-Date, 5-Description, 6-Cheque #, 7-Withdrawal, 8-Deposit
                        transaction = {
                            'description': row[5] + (' #'+row[6] if row[6] else ''),
                            'date': row[3].replace('/', '-'),
                            'amount': float(row[8] or '0') or -float(row[7] or '0')
                        }
                        output['transactions'].append(transaction)
            else:
                raise ValueError('Bank not supported yet!')

            for transaction in output['transactions']:
                transaction['description'] = StatementParser.clean_text(transaction['description'])

            return output
