package com.example.shoppingmall_project.model;

import com.example.shoppingmall_project.model.vo.HeaderVO;
import com.example.shoppingmall_project.model.vo.ProductVO;
import com.example.shoppingmall_project.model.vo.mypagevo.Paging;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface HeaderDAO {

    @Select("select categories_idx, categories_name " +
            "from categories " +
            "where parentcategory_idx = #{parentcategory_idx}")
    List<HeaderVO> getMenu(int idx);

    @Select("select img_url, products_idx " +
            "from products_img " +
            "order by products_idx, img_url")
    List<ProductVO> getImg();

    @Select("select p.products_idx, p.products_name, p.products_price, " +
            "c.categories_name, c.parentcategory_idx " +
            "from products p " +
            "join categories c on p.categories_idx = c.categories_idx " +
            "where c.categories_idx = #{idx} " +
            "order by p.products_idx desc")
    List<ProductVO> selectSubMenu(int idx);

    @Select("select banner_img_url " +
            "from banner_img " +
            "offset 0 rows " +
            "fetch first 5 rows only")
    List<ProductVO> getBanner();

    @Select("select p.products_idx, p.products_name, p.products_price, " +
            "c.categories_name, c.parentcategory_idx " +
            "from products p " +
            "join categories c on p.categories_idx = c.categories_idx " +
            "where parentcategory_idx = #{parentcategory_idx} " +
            "order by p.products_idx desc " +
            "offset 0 rows fetch first 12 rows only")
    List<ProductVO> selectMenu(int idx);

    @Select("select count(*) " +
            "from products p " +
            "join categories c on p.categories_idx = c.categories_idx " +
            "where c.parentcategory_idx = #{idx}")
    int menutotal(int idx);

    @Select("select p.products_idx, p.products_name, p.products_price, " +
            "c.parentcategory_idx " +
            "from products p " +
            "join categories c on p.categories_idx = c.categories_idx " +
            "where parentcategory_idx = 1 " +
            "order by p.products_idx desc " +
            "offset #{offset} rows " +
            "fetch first #{perCount} rows only")
    List<ProductVO> pagingMenu(Paging p);
}
