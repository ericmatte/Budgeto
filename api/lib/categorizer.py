import operator

from models import Category
from models import Keyword, add_to_db


class Categorizer:
    @staticmethod
    def find_category(description):
        """Take the description and try to associate it with a category
        It also add it to the keyword table, so that the admin can associate it to some categories
        """
        if not description:
            return None
        Keyword.generate_from_description(description)

        categories = {}
        for word in description.split(' '):
            keywords = Keyword.get_all(Keyword.value.contains(word))
            for keyword in keywords:
                if len(keyword.categories) > 0:
                    category_name = keyword.categories[0].name
                    categories[category_name] = 1 + categories.get(category_name, 0)

        if len(categories) == 0:
            return None
        else:
            category_name = max(categories.items(), key=operator.itemgetter(1))[0]
            return Category.get(name=category_name)
