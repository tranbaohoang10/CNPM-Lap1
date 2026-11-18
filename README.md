# Khung Selenium CineBook cho nhóm

Repo chỉ chuẩn bị thư mục và cấu hình. **Chưa có test case CineBook được viết sẵn.**
Nhóm tự triển khai F01–F08 theo Sheet và tab Thiết kế ca kiểm thử:
https://docs.google.com/spreadsheets/d/1fMU4NnRac8B86_i1nFsXD4N7bxilqZirKD_RNowpDPE/edit

## Cấu trúc

Trong `src/test/java/com/cinebook/`:

| Thư mục | Chức năng | Phụ trách theo Sheet |
|---|---|---|
| `base/` | BaseTest mở/đóng Chrome, URL, chờ dùng chung | Cả nhóm |
| `pages/` | Nhóm tự viết Page Objects | Cả nhóm |
| `utils/` | Nhóm tự viết tiện ích/minh chứng | Cả nhóm |
| `tests/f01/` | Đăng nhập | Nguyễn Đăng Hậu |
| `tests/f02/` | Tìm kiếm phim | Hoàng Trần Bảo |
| `tests/f03/` | Xem lịch chiếu | Hoàng Trần Bảo |
| `tests/f04/` | Chọn ghế và đặt vé | Bùi Văn Hiếu |
| `tests/f05/` | Thanh toán | Bùi Văn Hiếu |
| `tests/f06/` | Quản lý phim | Hoàng Lê Huy |
| `tests/f07/` | Quản lý lịch chiếu | Hoàng Lê Huy |
| `tests/f08/` | Quản lý đơn vé | Nguyễn Đăng Hậu |

`src/test/resources/selenium.properties`: URL web và chế độ Chrome.
`src/test/resources/testdata/`: nhóm tự thêm dữ liệu kiểm thử.

## Chạy

Yêu cầu JDK 17+, Chrome và web CineBook đang chạy. Maven Wrapper đã kèm theo repo.
Mở repo trong IntelliJ dưới dạng Maven project và Reload Maven.

Sau khi thành viên viết lớp `...Test extends BaseTest` vào `tests/fXX`:

```powershell
.\run-selenium.ps1
.\run-selenium.ps1 -Test TenLopTest
.\run-selenium.ps1 -Test 'TenLopTest#tenPhuongThuc'
.\run-selenium.ps1 -BaseUrl http://localhost:8080/CineBook_war_exploded -Headless
```

BaseTest không tự đăng nhập, thay dữ liệu hoặc kiểm tra nghiệp vụ. Khi chưa viết test,
Maven sẽ báo không có test để chạy; đây không phải kết quả Pass của F01–F08.
Bộ MyShop có sẵn giữ riêng, chỉ chạy khi chọn `mvnw.cmd -Pmyshop test`.

## Làm chung

- Tạo nhánh riêng theo chức năng, ví dụ `test/f02-search`, `test/f03-schedule`.
- Mỗi người viết test đúng thư mục được phân công; trao đổi trước khi đổi base/pages/utils.
- Dùng mã TC trong Sheet và giữ Expected đã ghi. Các ca chưa có mã cần được nhóm thống nhất.
- Tự chụp minh chứng khi thực thi và phân biệt kết quả cũ với kết quả chạy lại.
- Không commit `target/`, `out/`, mật khẩu thật hay file cấu hình riêng của máy.
