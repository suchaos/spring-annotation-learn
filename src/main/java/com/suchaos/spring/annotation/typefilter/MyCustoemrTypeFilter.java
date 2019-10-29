package com.suchaos.spring.annotation.typefilter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * TypeFilter，配合 {@link ComponentScan} 来进行使用
 *
 * TODO：待完成
 *
 * @author suchao
 * @date 2019/10/29
 */
public class MyCustoemrTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return false;
    }
}
