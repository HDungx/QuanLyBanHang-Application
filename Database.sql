--create database Nhom3_Agile_QLBH;

use Nhom3_Agile_QLBH;

create table NHANVIEN
(	manv nvarchar(7) primary key not null,
	tennv nvarchar(55),
	namsinh int,
	sdt nvarchar(10),
	ngaycong int,
	luong float
);

create table SANPHAM
	(	
	masp nvarchar(5) primary key not null,
	tensp nvarchar(55),
	soluong int,
	dongia float,
	);

create table HOADON
(
	mahd int primary key,
	tennguoiban nvarchar(50),
	ngaytaohd date,
	tongtien float);

create table HDCT
(	mahd int ,
	masp nvarchar(5),
	tensp nvarchar(50),
	dongia float,
	soluong int,
	foreign key (mahd) references hoadon(mahd)
);


	
create table LOGIN(
	username nvarchar(8) primary key,
	password nvarchar(8),
	role nvarchar(8),
	);
	
	

insert into login values('PS26548','1','admin'),('PS26555','1','user');

insert into NHANVIEN values
(N'PS26555',N'Nguyễn Hoàng Dũng',1999,'0901234567',15,240000),
(N'PS26562',N'Nguyễn Đình Dũng',2000,'0867989096',17,200000),
(N'PS26548',N'Nguyễn Ngọc Minh Thuận',1997,'0972345532',13,200000),
(N'PS26580',N'Trần Thụy Thúy Diễm',1999,'0232345532',20,230000),
(N'PS26440',N'Võ Trọng Quý',1996,'0901234567',18,240000),
(N'PS26524',N'Nguyễn Trọng Khoa',1997,'0901234567',25,270000);


insert into SANPHam values
('SP001',N'Nước Ngọt',7,200000),
('SP002',N'Mỳ Ăn Liền',20,160000),
('SP003',N'Gạo',12,250000),
('SP004',N'Ly Nhựa',100,80000),
('SP005',N'Muỗng',50,80000),
('SP006',N'Đũa',100,90000),
('SP007',N'Ống Hút',80,90000),
('SP008',N'Bia',20,140000),
('SP009',N'Kẹo Ngọt',30,10000),
('SP010',N'Thuốc Lá',20,25000),
('SP011',N'Bọc Đựng Rác ',25,30000),
('SP012',N'Khăn Giấy',9,25000),
('SP013',N'Trà',10,150000),
('SP014',N'Khăn lạnh',25,23000),
('SP015',N'Sữa',10,245000),
('SP016',N'Sữa Bột',0,300000),
('SP017',N'Bột Ngọt',0,12000);
