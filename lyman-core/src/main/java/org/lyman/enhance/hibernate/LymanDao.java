package org.lyman.enhance.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.lyman.exceptions.LymanErrorCode;
import org.lyman.exceptions.LymanException;
import org.lyman.utils.ArrayUtils;
import org.lyman.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "LymanDao")
public class LymanDao extends HibernateTemplate {

    public <T> List<T> find(@NonNull Class<T> clazz, @NonNull String hql,
                            @Nullable Object... params) {
        checkQueryString(hql);
        return executeWithNativeSession(s -> {
            Query<T> q = s.createQuery(hql, clazz);
            setQueryParameters(q, params);
            return q.getResultList();
        });
    }

    public List<?> findByNativeSql(@NonNull String sql, @Nullable Object... params) {
        checkQueryString(sql);
        return executeWithNativeSession(s -> {
            Query<?> q = s.createNativeQuery(sql);
            setQueryParameters(q, params);
            return q.list();
        });
    }

    public <T> T findOne(@NonNull Class<T> clazz, @NonNull String hql,
                         @Nullable Object... params) {
        checkQueryString(hql);
        return executeWithNativeSession(s -> {
            Query<T> q = s.createQuery(hql, clazz);
            setQueryParameters(q, params);
            try {
                return q.getSingleResult();
            } catch (NonUniqueResultException e) {
                log.error("Query result is not unique, {}", hql);
                List<T> list = q.getResultList();
                if (CollectionUtils.isNotEmpty(list))
                    return list.get(0);
                return null;
            }
        });
    }

    public <T> List<T> findLimit(@NonNull Class<T> clazz, @NonNull String hql, int limit, @Nullable Object... params) {
        return findLimitOffset(clazz, hql, limit, -1, params);
    }

    public <T> List<T> findLimitOffset(@NonNull Class<T> clazz, @NonNull String hql, int limit, int offset,
                                       @Nullable Object... params) {
        checkQueryString(hql);
        return executeWithNativeSession(s -> {
            Query<T> q = s.createQuery(hql, clazz);
            setQueryParameters(q, params);
            if (offset > 0) {
                q.setFirstResult(offset);
            if (limit >= 0)
                q.setMaxResults(limit);
            }
            return q.getResultList();
        });
    }

    public long count(@NonNull String hql, @Nullable Object... params) {
        checkQueryString(hql);
        return Optional.ofNullable(executeWithNativeSession(s -> {
            Query<Long> query = s.createQuery("select count(1) " + hql, Long.class);
            setQueryParameters(query, params);
            return query.getSingleResult();
        })).orElse(0L);
    }

    public void saveInBatch(Collection<? extends Serializable> entities) {
        if (CollectionUtils.isNotEmpty(entities)) {
            for (Object obj : entities) save(obj);
        }
    }

    public void insertOne(@NonNull String hql, @Nullable Object... params) {
        checkQueryString(hql);
        executeWithNativeSession(s -> {
            Query<?> query = s.createQuery(hql);
            setQueryParameters(query, params);
            return query.executeUpdate();
        });
    }

    //=====================|--inner--tools--|=========================

    private static void checkQueryString(String query) {
        if (StringUtils.isEmpty(query))
            throw new LymanException(LymanErrorCode.ARGUMENT_MISSSING);
    }

    private static void setQueryParameters(@NonNull Query<?> q, @Nullable Object... params) {
        if (ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; i++) {
                Object obj = params[i];
                if (obj instanceof Collection)
                    q.setParameterList(i, (Collection) obj);
                else if (obj instanceof Object[])
                    q.setParameterList(i, (Object[]) obj);
                else
                    q.setParameter(i, obj);
            }
        }
    }

}
