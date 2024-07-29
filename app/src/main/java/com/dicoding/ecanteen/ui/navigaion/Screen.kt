package com.dicoding.ecanteen.ui.navigaion

sealed class Screen(val route: String) {
    object Initial : Screen("initial")
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object MainPembeli : Screen("mainpembeli")
    object MainPenjual : Screen("mainpenjual")
    object AddMenu : Screen("addmenu")
    object OrdersPembeli : Screen("orderspembeli")
    object EwalletPembeli : Screen("ewalletpembeli")
    object EwalletPenjual : Screen("ewalletpenjual")
    object OrdersPenjual : Screen("orderspenjual")
    object ProfilePembeli : Screen("profilepembeli")
    object ProfilePenjual : Screen("profilepenjual")
    object Login : Screen("login")
    object RegisterPenjual : Screen("registerpenjual")
    object RegisterPembeli : Screen("registerpembeli")
    object LoginPenjual : Screen("loginpenjual")
    object LoginPembeli : Screen("loginpembeli")
    object About : Screen("about")
    object HistoryPembeli : Screen("historypembeli")
    object HistoryPenjual : Screen("historypenjual")
    object HomePembeli : Screen("homepembeli")
    object HomePenjual : Screen("homepenjual")
    object SuccessTopUp : Screen("successtopup")
    object SuccessOrder : Screen("successorder")
    object EditMenu : Screen("editmenu/{menuId}") {
        fun createRoute(menuId: Int) = "editmenu/$menuId"
    }

    object DetailMenuPenjual : Screen("detailmenupenjual/{menuId}") {
        fun createRoute(menuId: Int) = "detailmenupenjual/$menuId"
    }

    object DetailMenuPembeli : Screen("detailmenupembeli/{menuId}") {
        fun createRoute(menuId: Int) = "detailmenupembeli/$menuId"
    }

    object MenuOrder : Screen("menuorder/{menuId}") {
        fun createRoute(menuId: Int) = "menuorder/$menuId"
    }

    object TopUp : Screen("topup/{saldoId}") {
        fun createRoute(saldoId: Int) = "topup/$saldoId"
    }

    object DetailPesananPembeli : Screen("detailpesananpembeli/{pesananId}") {
        fun createRoute(pesananId: Int) = "detailpesananpembeli/$pesananId"
    }

    object DetailPesananPenjual : Screen("detailpesananpenjual/{pesananId}") {
        fun createRoute(pesananId: Int) = "detailpesananpenjual/$pesananId"
    }

    object Transaction : Screen("transaction?orderId={orderId}&menuId={menuId}&nama={nama}&gambar={gambar}&pembeliId={pembeliId}&penjualId={penjualId}&quantity={quantity}&catatan={catatan}&jam_pesan={jam_pesan}&harga={harga}&jml_harga={jml_harga}&status={status}") {
        fun createRoute(
            orderId: String,
            menuId: Int,
            nama: String,
            gambar: String,
            pembeliId: Int,
            penjualId: Int,
            quantity: Int,
            catatan: String,
            jam_pesan: String,
            harga: Int,
            jml_harga: Int,
            status: Boolean
        ): String {
            return "transaction?orderId=$orderId&menuId=$menuId&nama=$nama&gambar=$gambar&pembeliId=$pembeliId&penjualId=$penjualId&quantity=$quantity&catatan=$catatan&jam_pesan=$jam_pesan&harga=$harga&jml_harga=$jml_harga&status=$status"
        }
    }

    object EditProfilePembeli : Screen("editProfilePembeli?id={id}&fullName={fullName}&email={email}&telp={telp}") {
        fun createRoute(
            id: Int,
            fullName: String,
            email: String,
            telp: String,
        ): String {
            return "editProfilePembeli?id=$id&fullName=$fullName&email=$email&telp=$telp"
        }
    }

    object EditProfilePenjual : Screen("editProfilePenjual?id={id}&fullName={fullName}&email={email}&telp={telp}&namaToko={namaToko}&deskripsi={deskripsi}") {
        fun createRoute(
            id: Int,
            fullName: String,
            email: String,
            telp: String,
            namaToko: String,
            deskripsi: String,
        ): String {
            return "editProfilePenjual?id=$id&fullName=$fullName&email=$email&telp=$telp&namaToko=$namaToko&deskripsi=$deskripsi"
        }
    }

}