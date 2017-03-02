#oracle笔记
##第一课：客户端
1. Sql Plus(客户端），命令行直接输入：sqlplus，然后按		提示输入用户名，密码。
2. 从开始程序运行:sqlplus，是图形版的sqlplus.
3. http://localhost:5560/isqlplus
Toad：管理， PlSql Developer:


##第二课：更改用户
```sql
sqlplus sys/bjsxt as sysdba
alter user scott account unlock;(解锁)
```

##第三课：table structure
```sql
desc emp; --描述一张表
select * from emp;
```
##第四课：select 语句：
```sql
select 2*3 from dual --计算数据用空表dual

select ename,sal*12 annual_sal from emp;
select ename,sal*12 "annual sal" from emp;

--区别，加双引号保持原大小写。不加全变大写。
       
select ename || "abcd" 
--如果连接字符串中含有单引号，用两个单引号代替一个单引号。
```

##第五课：distinct
```sql
select deptno from emp;
select distinct deptno from emp;
select distinct deptno from emp;
select distinct deptno ,job from emp
--用distinct去掉deptno,job两者组合的重复。
--更多的项，就是这么多项的组合的不重复组合。
```
##第六课：Where
```sql
select * from emp where deptno =10;
select * from emp where deptno <>10;  --不等于10        
select * from emp where ename ='bike';
select ename,sal from emp where sal 
	between 800 and 1500 (>=800 and <=1500);
--空值处理:
select ename,sal,comm from emp where comm is null;
select ename,sal,comm from emp where ename in ('smith','king','abc');
select ename from emp where ename like '_A%';
--_代表一个字母,%代表0个或多个字母. 如果查询%
--可用转义字符.\%. 还可以用escape '$'比如:select ename from emp where ename like '%$a%' escape '$';
```
##第七课: orderby
```sql       
select * from dept; 
select * from dept order by dept desc;--(默认:asc)
select ename,sal,deptno from emp order by deptno asc,ename desc;
```

##第八课: sql function1:    
```sql
select ename,sal*12 annual_sal from emp
where ename not like '_A%' and sal>800
order by sal desc;

select lower(ename) from emp;

select ename from emp 
where lower(ename) like '_a%';
--等同于
select ename from emp where ename like '_a%' or ename like '_A%';

select substr(ename,2,3) from emp;
--从第二字符截,一共截三个字符.
select chr(65) from dual --结果为:A
select ascii('a') from dual --结果为:65
select round(23.652,1) from dual; --结果为: 23.7
select round(23.652,-1) from dual; --20
  
select to_char(sal,'$99_999_999') from emp;
select to_char(sal,'L99_999_999') from emp;
--人民币符号,L:代表本地符号


这个需要掌握牢:
select birthdate from emp;
显示为:
        BIRTHDATE
        ----------------
        17-12月-80
        ----------------

改为:
select to_char(birthdate,'YYYY-MM-DD HH:MI:SS') from emp;
        
        显示:
         
        BIRTHDATE
        -------------------
        1980-12-17 12:00:00
        -------------------
        
        select to_char(sysdate,'YYYY-MM-DD HH24:MI:SS') from dual; //也可以改为:HH12
        TO_CHAR(SYSDATE,'YY
        -------------------
        2007-02-25 14:46:14
        

        to_date函数:
        select ename,birthdate from emp where birthdate > to_date('1981-2-20 12:34:56','YYYY-MM-DD HH24:MI:SS');
        如果直接写 birthdate>'1981-2-20 12:34:56'会出现格式不匹配,因为表中的格式为: DD-MM月-YY.
  
        
        select sal from emp where sal>888.88 无错.但
        select sal from emp where sal>$1,250,00;
        会出现无效字符错误. 
        改为:
        select sal from emp where sal>to_number('$1.250.00','$9,999,99');
        
        --把空值改为0
        select ename,sal*12+nvl(comm,0) from emp;
        --这样可以防止comm为空时,sal*12相加也为空的情况.
```

##第九课: Group function 组函数
        max,min,avg ,count,sum函数
        
        select to_char(avg(sal),'99999999,99') from emp;
        
        
        select round(avg(sal),2) from emp;
        结果:2073.21
          
        select count(*) from emp where deptno=10;
        select count(ename) from emp where deptno=10; count某个字段,如果这个字段不为空就算一个.
        select count(distinct deptno) from emp;
        select sum(sal) from emp;

##第十课: Group by语句
        
        需求:现在想求,求每个部门的平均薪水.
        select avg(sal) from emp group by deptno;
        select deptno avg(sal) from emp group by deptno;
        
        select deptno,job,max(sal) from emp group by deptno,job;
        
       求薪水值最高的人的名字.
       select ename,max(sal) from emp;出错,因为max只有一个值,但等于max值的人可能好几个,不能匹配.
       应如下求:
       select ename from emp where sal=(select max(sal) from emp);

       Group by语句应注意,

       出现在select中的字段,如果没出现在组函数中,必须出现在Group by语句中.
        
    
##第十一课: Having 对分组结果筛选
       
       Where是对单条纪录进行筛选,Having是对分组结果进行筛选.
      
       select avg(sal),deptno from emp 
       group by deptno 
       having avg(sal)>2000;
       
       查询工资大于1200雇员,按部门编号进行分组,分组后平均薪水大于1500,按工薪倒充排列.
       select * from emp 
       where sal>1200
       group by deptno
       having avg(sal)>1500
       order by avg(sal) desc;
        
##第十二课:字查询
       
       谁挣的钱最多(谁:这个人的名字,  钱最多)
       
       select 语句中嵌套select 语句,可以在where,from后.
       
             
       问那些人工资,在平均工资之上.
       
       select ename,sal from emp where sal>(select avg(sal) from emp);


       查找每个部门挣钱最多的那个人的名字.
       select ename ,deptno from emp where sal in(select max(sal) from ename group by deptno) 查询会多值.

       应该如下:
       
       select  max(sal),deptno from emp group by deptno;当成一个表.语句如下:
       select ename, sal from emp join(select  max(sal) max_sal,deptno from emp group
       by deptno) t on(emp.sal=t.max_sal and emp.deptno=t.deptno); 
       
       每个部门的平均薪水的等级. 
       分析:首先求平均薪水(当成表),把平均薪水和另外一张表连接.
       
       
##第十四课:self_table_connection
       
       把某个人的名字以及他的经理人的名字求出来(经理人及这个人在表中同处一行)
       
       分析:首先求出这个人的名字,取他的编号,然后从另一张表与其相对应编号,然后找到经理的名字.
       
       select e1.ename ,e2.ename from emp e1,emp e2 where e1.mgr= e2.empno.
       
       empno编号和MGR都是编号.


##第十15课: SQL1999_table_connections    
         
      select ename,dname,grade from emp e,dept d, sqlgrade s
      where e.deptno = d.deptno and e.sql between s.losal and s.hisal and
      job<>'CLERK';
      
      有没有办法把过滤条件和连接条件分开来? 出于这样考虑,Sql1999标准推出来了.有许多人用的还是
      旧的语法,所以得看懂这种语句.
      
      
      
      select ename,dname from emp,dept;(旧标准).
      select ename,dname from emp cross join dept;(1999标准)
       
      select ename,dname from emp,dept where emp.deptno=dept.deptno (旧) 
      select ename,dname from emp join dept on(emp.deptno = dept.deptno); 1999标准.没有Where语句.
      select ename,dname from emp join dept using(deptno);等同上句,但不推荐使用.
      
      select ename,grade from emp e join salgrade s on(e.sal between s.losal and s.hisal);
      join 连接语句, on过滤条件。连接，条件一眼分开。如果用Where语句较长时，连接语句和过滤语句混在一起。
      
      三张表连接：
      slect ename,dname, grade from 
      emp e join dept d on(e.deptno=d.deptno)
      join salgrade s on(e.sal between s.losal and s.hisal)
      where ename not like '_A%';
      把每张表连接 条件不混在一起，然后数据过滤条件全部区分开来。读起来更清晰，更容易懂一点。
      
      select e1.ename,e2.ename from emp e1 join emp e2 on(e1.mgr = e2.emptno);

      左外连接：会把左边这张表多余数据显示出来。
      select e1.ename,e2,ename from emp e1 left join emp e2 on(e1.mgr =e2.empno);left 后可加outer
      右外连接：
    select ename,dname from emp e right outer join dept d on(e.deptno =d.deptno); outer可以取掉。
        
      即把左边多余数据，也把右边多余数据拿出来，全外连接。
      select ename,dname from emp e full join dept d on(e.deptno =d.deptno); 


##16-23 课：求部门平均薪水的等级
```sql
       A.求部门平均薪水的等级。

       select deptno,avg_sal,grade from 
       (select deptno,avg(sal) avg_sal from emp group by deptno)t
       join salgrade s on(t.avg_sal between s.losal and s.hisal)
       
       B.求部门平均的薪水等级
       select deptno,avg(grade) from 
       (select deptno,ename, grade from emp join salgrade s on(emp.sal between s.losal and
       s.hisal)) t
       group by deptno

       C.那些人是经理
       select ename from emp where empno in(select mgr from emp);
       select ename from emp where empno in(select distinct mgr from emp);
       
       D.不准用组函数，求薪水的最高值（面试题）
       
       select distinct sal from emp where sal not in(
       select distinct e1.sal from emp e1 join emp e2 on (e1.sal<e2.sal));
       
       E.平均薪水最高的部门编号
       
       select deptno,avg_sal from
       (select avg(sal)avg_sal,deptno from emp group by deptno)
       where avg_sal=
       (select max(avg_sal)from 
       (select avg(sal) avg_sal,deptno from emp group by deptno)
       )
      
       F.平均薪水最高的部门名称
       select dname from dept where deptno=
      ( 
        select deptno from
        (select avg(sal)avg_sal,deptno from emp group by deptno)
        where avg_sal=
        (select max(avg_sal)from 
        (select avg(sal) avg_sal,deptno from emp group by deptno)
        )
       )
      
      G.求平均薪水的等级最低的部门的部门名称
        
        组函数嵌套
        如：平均薪水最高的部门编号，可以E.更简单的方法如下：
        select deptno,avg_sal from 
        (select avg(sal) avg_sal,deptno from emp group by deptno)
        where avg_sal =
        (select max(avg(sal)) from emp group by deptno)
        
        组函数最多嵌套两层
        
        分析：
        首先求
        1.平均薪水： select avg(sal) from group by deptno;

        2.平均薪水等级：  把平均薪水当做一张表，需要和另外一张表连接salgrade
        select  deptno,grade avg_sal from 
          ( select deptno,avg(sal) avg_sal from emp group by deptno) t
        join salgrade s on(t.avg_sal between s.losal and s.hisal)
        
        上面结果又可当成一张表。
        
        DEPTNO    GRADE    AVG_SAL
      --------  -------  ----------
        30           3   1566.66667
        20           4   2175
        10           4   2916.66667

        3.求上表平均等级最低值
        
        select min(grade) from
        (
          select deptno,grade,avg_sal from
           (select deptno,avg(sal) avg_sal from emp group by deptno)t
          join salgrade s on(t.avg_sal between s.losal and s.hisa)
         )

        4.把最低值对应的2结果的那张表的对应那张表的deptno, 然后把2对应的表和另外一张表做连接。
          
          select dname ,deptno,grade,avg_sal from
            (
  	      select deptno,grade,avg_sal from
              (select deptno,avg(sal) avg_sal from emp group by deptno)t
             join salgrade s on(t.avg_sal between s.losal and s.hisal)
             ) t1
            join dept on (t1.deptno = dept.deptno)
            where t1.grade =
            ( 
              select deptno,grade,avg_sal from
               (select deptno,avg(sal) avg_sal from emp group by deptno) t
                join salgrade s on(t.avg_sal between s.losal and s.hisal)
               )
            )
         结果如下：
         
        DNAME    DEPTNO     GRADE    AVG_SAL
      --------  -------  --------   --------
        SALES        30        3    1566.6667 
     
         
       H: 视图（视图就是一张表，一个字查询）
        
       G中语句有重复，可以用视图来简化。
       conn sys/bjsxt as sysdba;
       grant create table,create view to scott;
       conn scott/tiger
       创建视图：
       create view v$_dept_avg-sal_info as
       select deptno,grade,avg_sal from
        ( select deptno,avg(sal) avg_sal from emp group by deptno)t
       join salgrade s on 9t.avg_sal between s.losal and s.hisal)
      
       然后 
       select * from v$_dept_avg-sal_info
       
       结果如下：
       DEPTNO      GRADE    AVG_SAL
      --------  -------  ----------
        30           3   1566.66667
        20           4   2175
        10           4   2916.66667

       然后G中查询可以简化成：
       select  dname,t1.deptno,grade,avg_sal from
       v$_dept_avg-sal_info t1
       join dept on9t1.deptno =dept.deptno)
       where t1.grade=
       (
	select min(grade) from v$_dept_avg-sal_info t1
       ) 
—————————

—-backup scott （备份原来scott用户里的东西）
回到根目录：cd /
去temp目录：cd temp
删除该目录下所有文件：del *.*;
开始输出备份：exp
一路enter，用户名写scott／密码
—create users
Sqlplus目录下以超级管理员身份登陆： conn sys/ocrl as sysdba;
Create user licheng identified by licheng default tablespace user quote 10M on users;
用户已创建。
Grant create session, create table, create view to licheng;
授权成功。
—import the data
回到temp目录下：imp
第一个用户名输：licheng/licheng
第二输：scott

以上成功完成备份，创建新用户操作 

—————————


后悔撤销操作：rollback;
创建备份表：create table emp2 as select * from emp;
	create table dept2 as select * from dept;
	create table salgrade2 as select * from salgrade;


取出薪水的第六到第10:
select ename, sal from(	select ename, sal, rownum r from		(select ename, sal from emp order by sal desc))where r >= 6 and r <= 10


—update语句
	update emp2 set sal = sal*2, ename = ename||’-‘ where deptno = 10;

-delete语句
	delete from emp2;


```
##——DBL语言-数据库定义语言

##—表
```sql
-create table
	create table t (a varchar2(10));
表已创建。

-drop table
	drop table t;
表已删除。

-commit提交
	commit;
提交改变，把insert，delete,update的改变提交，无法回退。
（在未提交前，一系列改变在rollback后会全部撤销）。


-当正常断开时，即
	exit;
自动提交chansection（事件）。
若非正常断开，即直接关闭窗口，chansection自动回滚。


—create table
	create table stu
	(
	id number(6),
	name varchar2(20),
	sex number(1),
	age number(3),
	sdate date,
	grade number(2) default 1,     //指定默认值为1，而不是空
	class number(4),
	email varchar2(50)
	);

——constraint约束条件5种：非空(not null)、唯一(unique)、主键(primary key)(作用相当于非空加唯一，还有逻辑上的约束）、外键(涉及两张表,references table(sth)，被参考的字段必须是主键)，、check
	跟在一个字段后面，字段约束
	若是表级约束，加在最后
	约束起名字：constraint + 名字 +约束条件
create table stu(	id number(6) primary key,	name varchar2(20) constraint stu_name_nn not null,	sex number(1),	age number(3),	sdate date,	grade number(2) default 1,    	class number(4) references class(id),	email varchar2(50),
	//constraint stu_id_pk primary key(id),
	//constraint stu_class_fk foreign key(class) references class(id),	constraint stu_name_email_uni unique(name, email))；

create table class
(
id numebr(4) primary key,
name varchar2(20) not null
);


—更改表中的某一字段
alter table stu drop(addr);
alter table stu add(addr varchar2(100));
alter table stu modify(addr varchar2(50));(注意精度必须能够容纳原有数据）；
alter table stu drop constraint stu_class_fk;（去掉约束条件）；
alter table stu add constraint stu_class_fk foreign key(class) references class(id);
delete from class;（删除表中记录最后一行）；


—数据字典表
desc user_tables;
select table_name from user_tables;
select view_name from user_views;
select constraint_name from user_constraints;
desc dictionary;
select * from dictionary;

-索引应用
create index idx_stu_email on stu(email);
drop index idx_stu_email;
select index_name from user_indexes;
当建立索引后，读起来快了，修改起来慢了。

-视图（虚表）
默认是一个字查询。
create view V$….
视图建立以后，当表结构更改后，会增加维护的支出。


-序列
create table article
(
id number,
title varchar2(1024),
cont long
);
表已创建。
create sequence seq;
序列已创建。
Select seq.nextval from dual;（保持内部线程同步，即递增地产生唯一的数）。
insert into article values (seq.nextval, ‘a’, ‘b’);


———数据库设计的三范式
范式：数据库设计的规则（姓范的制定的）。
追求的原则：不存在冗余数据（同样的数据不存第二遍）
第一范式：
第一要求：任何一个表都要有主键。
第二要求：列不可分。不能用0980_张三_23之类的。
第二范式：
当一张表中有多个字段作为主键时，那些不是主键的字段不能依赖部分主键，即不能部分依赖。
第三范式：
在第二范式的基础上，数据表中如果不存在非主键字段对任一候选主键字段的传递函数依赖则符合第三范式。

第一 范式就是数据库表中字段不可再分，这个好解释。比如一个表用来记录个人信息，我就给你一个字段 “人” 很明显这样不合理这个字段可以再分为 姓名，性别什么的。1nf就是原子性（不可再分）。
第二范式 通俗点讲就是用关键字可以确定唯一的一条记录。例如 我把一个人的身份证号设为关键字，那么我可以用411322xxxxxx这个身份证号找到对应的这个人（唯一的），但是我如果把姓名设为主键很明显我搜索张三会找到一大堆张三。2nf就是唯一性
第三范式：3NF是对字段冗余性的约束，即任何字段不能由其他字段派生出来，它要求字段没有冗余。

三大范式并不是用来区别的，是关系型数据库里的规范，是为了减少数据冗余。如果三个规范都满足说明的你的数据库比较健全，数据冗余少，后期维护也方便。用多了就知道了。如果一定要记下，记住定义就好。第一范式:确保每列的原子性.
    如果每列(或者每个属性)都是不可再分的最小数据单元(也称为最小的原子单元),则满足第一范式.
    例如:顾客表(姓名、编号、地址、……)其中"地址"列还可以细分为国家、省、市、区等。
第二范式:在第一范式的基础上更进一层,目标是确保表中的每列都和主键相关.
    如果一个关系满足第一范式,并且除了主键以外的其它列,都依赖于该主键,则满足第二范式.
    例如:订单表(订单编号、产品编号、定购日期、价格、……)，"订单编号"为主键，"产品编号"和主键列没有直接的关系，即"产品编号"列不依赖于主键列，应删除该列。
第三范式:在第二范式的基础上更进一层,目标是确保每列都和主键列直接相关,而不是间接相关.
    如果一个关系满足第二范式,并且除了主键以外的其它列都不依赖于主键列,则满足第三范式.
    为了理解第三范式，需要根据Armstrong公里之一定义传递依赖。假设A、B和C是关系R的三个属性，如果A-〉B且B-〉C，则从这些函数依赖中，可以得出A-〉C，如上所述，依赖A-〉C是传递依赖。


——plsql，用来写oracle内部的一些程序，实现触发器等功能
Pl：补充sql语言，sql语言没有分支没有循环语言。

先写set serveroutput on;设置环境变量让其显示输出。

尝试：
begin dbms_output.put_line('HelloWorld');end;/

--异常抓取
declare	v_num number := 0;begin	v_num := 2/v_num;	dbms_output.put_line(v_num);exception	when others then	dbms_output.put_line('error');end;


—常用变量类型
1.	binary_integer:整数，主要用来计数而不是用来表示字段类型
2.	number:数字类型
3.	char:定长字符串
4.	varchar2:变长字符串
5.	date:日期
6.	long:长字符串，最长2GB
7.	boolean:布尔类型，可以取值为true、false和null值（特别注意）

—变量声明
declare
	v_temp number(1);
	v_count binary_integer := 0;
	v_sal number(7,2) := 4000.00;
	v_date date := sysdate;
	v_pi constant number(3,2) := 3.14;
	v_valid boolean := false;
	v_name varchar2(20) not null := ‘MyName’;
begin
	dbms_output.put_line(‘v_temp value:’ || v_temp);
end;

--变量声明，使用%type属性
 	declare（声明变量）
	  v_empno number(4);
	  v_empno2 emp.empno%type;
	  v_empno3 v_empno2%type;
	begin
	  dbms_output.put_line('test');
	end;

--table变量类型(数组)
declare
   type type_table_emp_empno is table of emp.empno%type index by binary_integer;
      v_empnos type_table_emp_empno;
begin
   v_empnos(0) := 7369;
    v_empnos(2) := 7839;
    v_empnos(-1) := 9999;
    dbms_output.put_line(v_empnos(-1));
end;


--record变量类型（近似java中的类）
declare
  type type_record_dept is record
      (
        deptno dept.deptno%type,
        dname dept.dname%type,
        loc dept.loc%type
      );
    v_temp type_record_dept;
begin
  v_temp.deptno := 50;
  v_temp.dname := 'aaa';
  v_temp.loc := 'bj';
  dbms_output.put_line(v_temp.deptno || ' ' || v_temp.dname);
end;


--使用%rowtype声明record变量
declare
  v_temp dept%rowtype;
begin
    v_temp.deptno := 50;
    v_temp.dname := 'aaa';
    v_temp.loc := 'bj';
   dbms_output.put_line(v_temp.deptno || ' ' || v_temp.dname);
end;


--SQL语句的运用
--select语句,plsql中必须返回一条记录且只能返回一条记录。
declare
     v_name emp.ename%type;
     v_sal emp.sal%type;
begin
   select ename, sal into v_name, v_sal from emp where empno = 7369;
   dbms_output.put_line(v_name || ' ' || v_sal);
end;


declare
    v_emp emp%rowtype;
begin
  select * into v_emp from emp where empno = 7369;
  dbms_output.put_line(v_emp.ename);
end;

--insert语句

declare
    v_deptno dept.deptno%type := 50;
    v_dname dept.dname%type := 'aaa';
    v_loc dept.loc%type := 'bj';
begin
  insert into dept2 values(v_deptno, v_dname, v_loc);
 commit;
end;

declare
   v_deptno emp2.deptno%type := 10;
   v_count number;
begin
  --update emp2 set sal = sal/2 where deptno = v_deptno;
  --select deptno into v_deptno from emp2 where empno = 7369;
  —-select count(*) into v_count from emp2;
  dbms_output.put_line(sql%rowcount || '条记录被影响');
 commit;
end;

DDL语句
begin
     execute immediate 'create table t (nnn varchar2(20) default ''aaa'')';
end;


--if语句
取出7369的薪水，如果<1200，输出'low'，如果<2000输出'middle'，否则'high'

declare
    v_sal emp.sal%type;
begin
   select sal into v_sal from emp
          where empno = 7369;
   if (v_sal < 1200) then
         dbms_output.put_line('low');
   elsif (v_sal < 2000) then
         dbms_output.put_line('middle');
   else
         dbms_output.put_line('high');
   end if;
end;

--练习



--循环
declare
   i binary_integer := 1;
begin
   loop
      dbms_output.put_line(i);
           i := i + 1;
           exit when (i >= 11);
   end loop;
end;
---------
declare
   j binary_integer := 1;
begin
  while j < 11 loop
      dbms_output.put_line(j);
          j := j + 1;
  end loop;
end;

-----------
begin
    for k in 1..10 loop
       dbms_output.put_line(k);
    end loop;

    for k in reverse 1..10 loop
        dbms_output.put_line(k);
     end loop;
end;

--错误处理
declare
   v_temp number(4);
begin
   select empno into v_temp from emp where deptno = 10;
exception
   when too_many_rows then
      dbms_output.put_line('太多纪录了');
   when others then
      dbms_output.put_line('error');
end;

----------

declare
   v_temp number(4);
begin
   select empno into v_temp from emp where empno = 2222;
exception
   when no_data_found then
      dbms_output.put_line('没有数据');
end;

---------
--创建事件日志表
create table errorlog
(
id number primary key,
errcode number,
errmsg varchar2(1024),
errdate date
)
--创建序列
create sequence seq_errorlog_id start with 1 increment by 1 
--实验
declare
   v_deptno dept.deptno%type := 10;
   v_errcode number;
   v_errmsg varchar2(1024);
begin
   delete from dept where deptno = v_deptno;
 commit;
exception
   when others then
      rollback;
         v_errcode := SQLCODE;
         v_errmsg := SQLERRM;
      insert into errorlog values (seq_errorlog_id.nextval, v_errcode, v_errmsg, sysdate);
      commit;
end;

--游标
declare
   cursor c is
            select * from emp;
   v_temp c%rowtype;
begin
    open c;
    fetch c into v_temp;
    dbms_output.put_line(v_temp.ename);
    close c;
end;

------------------
declare
    cursor c is
       select * from emp;
    v_emp c%rowtype;
begin
    open c;
    loop
      fetch c into v_emp;
      exit when (c%notfound);
      dbms_output.put_line(v_emp.ename);
    end loop;
    close c;
end;
----------------------
declare
    cursor c is
       select * from emp;
    v_emp c%rowtype;
begin
    open c;
    fetch c into v_emp;
    while (c%found) loop
      dbms_output.put_line(v_emp.ename);
      fetch c into v_emp;
    end loop;
    close c;
end;
-----------------
declare
    cursor c is
       select * from emp;
begin
   for v_emp in c loop
        dbms_output.put_line(v_emp.ename);
    end loop;
end;

--带参数的游标
declare
   cursor c (v_deptno emp.deptno%type, v_job emp.job%type)
   is
     select ename, sal from emp where deptno = v_deptno and job = v_job;
begin
   for v_temp in c(30,'CLERK') loop
      dbms_output.put_line(v_temp.ename);
   end loop;
end;

--可更新的游标
declare
  cursor c
  is
    select * from emp2 for update;
begin
   for v_temp in c loop
      if (v_temp.sal < 2000) then
         update emp2 set sal = sal * 2 where current of c;
      elsif (v_temp.sal = 5000) then
         delete from emp2 where current of c;
      end if;
    end loop;
    commit;
end;
----------------
--存储过程
create or replace procedure p
--创建了一个存储过程
is
  cursor c
  is
    select * from emp2 for update;
begin
   for v_temp in c loop
      if (v_temp.deptno = 10) then
         update emp2 set sal = sal + 10 where current of c;
      elsif (v_temp.deptno = 20) then
         update emp2 set sal = sal + 20 where current of c;
      else
         update emp2 set sal = sal + 50 where current of c;
      end if;
    end loop;
    commit;
end;
--执行 
exec p;
或
begin
 p;
end;
--带参数的存储过程（in叫传入参数，out传出参数，不写默认in）
create or replace procedure p
     (v_a in number, v_b number, v_ret out number, v_temp in out number)
is
begin
   if (v_a > v_b) then
      v_ret := v_a;
   else
      v_ret := v_b;
   end if;
   v_temp := v_temp + 1;
end;

--实验
declare
 v_a number := 3;
 v_b number := 4;
 v_ret number;
 v_temp number := 5;
begin
 p(v_a, v_b, v_ret, v_temp);
 dbms_output.put_line(v_ret);
 dbms_output.put_line(v_temp);
end;
-------------------
--函数
create or replace function sal_tax
  (v_sal number)
  return number
is
begin
   if (v_sal < 2000) then
      return 0.10;
   elsif (v_sal < 2750) then
      return 0.15;
   else
      return 0.20;
   end if;
end；

select lower(ename),sal_tax(sal) from emp；

--触发器
create table emp2_log
(
uname varchar2(20),
action varchar(10),
atime date
)
-----------
create or replace trigger trig
  after insert or update or delete on emp2
begin
  if inserting then
     insert into emp2_log values (USER, 'insert', sysdate);
  elsif updating then
     insert into emp2_log values (USER, 'update', sysdate);
  elsif deleting then
     insert into emp2_log values (USER, 'delete', sysdate);
  end if;
end;

----------
update emp2 set sal = sal * 2 where deptno = 30;
--------
create or replace trigger trig
  after insert or update or delete on emp2 for each row
begin
  if inserting then
     insert into emp2_log values (USER, 'insert', sysdate);
  elsif updating then
     insert into emp2_log values (USER, 'update', sysdate);
  elsif deleting then
     insert into emp2_log values (USER, 'delete', sysdate);
  end if;
end;
-------------
--不提倡使用
create or replace trigger trig
 after update on dept for each row
begin
 update emp2 set deptno = :NEW.deptno where deptno = :OLD.deptno;
end;
----------------------
--树状结构的存储与展现
drop table article;

create table article
(
id number primary key,
cont varchar2(4000),
pid number,
isleaf number(1), --0代表非叶子节点，1代表叶子节点
alevel number(2)
)
-------------
insert into article values (1, '蚂蚁大战大象', 0, 0, 0);
insert into article values (2, '大象被打趴下了', 1, 0, 1);
insert into article values (3, '蚂蚁也不好过', 2, 1, 2);
insert into article values (4, '瞎说', 2, 0, 2);
insert into article values (5, '没有瞎说', 4, 1, 3);
insert into article values (6, '怎么可能', 1, 0, 1);
insert into article values (7, '怎么没可能', 6, 1, 2);
insert into article values (8, '可能性是很大的', 6, 1, 2);
insert into article values (9, '大象进医院了', 2, 0, 2);
insert into article values (10, '护士是蚂蚁', 9, 1, 3);
commit;
---------
蚂蚁大战大象
   大象被打趴下了
      蚂蚁也不好过
      瞎说
         没有瞎说
      大象进医院了
         护士是蚂蚁
   怎么可能
         怎么不可能
         可能性是很大的
--------------------------
create or replace procedure p (v_pid article.pid%type, v_level binary_integer) is
  cursor c is select * from article where pid = v_pid;
  v_preStr varchar2(1024) := '';
begin
  for i in 1..v_level loop
    v_preStr := v_preStr || '****';
  end loop;
  for v_article in c loop
    dbms_output.put_line(v_preStr || v_article.cont);
  if (v_article.isleaf = 0)
then
    p (v_article.id, v_level + 1);
  end if;
  end loop;
end;
```