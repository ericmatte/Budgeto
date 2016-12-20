import sqlalchemy.orm.properties
from sqlalchemy.sql.functions import func

from endless.server.database import DeclarativeBase

metadata = DeclarativeBase.metadata


class BaseEntity:
    @classmethod
    def get(cls, **kwargs):
        q = cls.query
        q = cls.apply_filter(q, **kwargs)
        return q.one()

    @classmethod
    def get_all(cls, **kwargs):
        q = cls.query
        q = cls.apply_filter(q, **kwargs)
        return q.all()

    # Override in class for specific filtering
    @classmethod
    def apply_filter(cls, query, **kwargs):
        query = query.filter_by(**kwargs)
        return query

    @classmethod
    def apply_sorting(cls, query, **kw):
        if 'sidx' in kw and kw['sidx']:
            sort_idx = kw['sidx']
            sort_ord = kw['sord']
            if hasattr(cls, sort_idx):
                attr = getattr(cls, sort_idx)

                if isinstance(attr.property, sqlalchemy.orm.properties.RelationshipProperty):
                    query = query.join(attr)
                    attr = attr.property.argument.getSortingAttribute()

                if sort_ord == 'asc':
                    query = query.order_by(attr)
                else:
                    query = query.order_by(attr.desc())

        return query

    @classmethod
    def get_count(cls, **kwargs):
        q = cls.session.query(func.count(next(iter(cls.__table__.primary_key.columns))))
        q = cls.apply_filter(q, **kwargs)
        return q.scalar()

    @classmethod
    def get_page_rows(cls, start_idx, end_idx, **kwargs):
        q = cls.query
        q = cls.apply_filter(q, **kwargs)
        q = q.slice(start_idx, end_idx)
        return q.all()
