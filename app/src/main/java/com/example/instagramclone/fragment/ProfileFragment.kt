package com.example.instagramclone.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.instagramclone.AccountSetting
import com.example.instagramclone.R
import com.example.instagramclone.User
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileFragment : Fragment() {

    // ViewBinding Fragment untuk fragment_profile.xml
    private lateinit var binding: FragmentProfileBinding

    // buat variabel databaseReference berisi database reference firebase
    // database reference adalah bagian realtime database dari firebase
    private lateinit var databaseReference: DatabaseReference

    // buat variabel user yang berisi model user dari struktur data di firebase
    private lateinit var user: User

    // onCreateView dipakai untuk menginisialisasi view yang ada pada layout fragment_profile
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inisialisasi viewbinding
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    // onViewCreated untuk memberi fungsi pada view di dalam layout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // beri fungsi setonclicklistener pada tombol edit profil
        binding.btnEditProfil.setOnClickListener {
            // buat intent menuju Akun Setting
            val intent = Intent(view.context, AccountSetting::class.java)
            startActivity(intent)
        }

        // cek adanya user yang login saat ini
        FirebaseAuth.getInstance().currentUser?.let { currentUser ->
            // dapatkan UID dari User yang login
            val uidUser = currentUser.uid
            // dapatkan Database Reference berdasarkan UID
            databaseReference = FirebaseDatabase.getInstance()
                .reference
                // child users berisi nama folder dari user yang ada di firebase realtime database
                // nama ini harus persis
                .child("users")
                .child(uidUser)

            // mengambil data user berdasarkan
            databaseReference.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            user = snapshot.getValue(User::class.java) as User
                            // gunakan binding biar cepet untuk memasukkan data pada UI
                            binding.run {
                                // masukkan data pada textview
                                profileName.text = user.username
                                deskripsi.text = user.Bio
                                nama.text = user.fullname

                                // jika user tidak memiliki foto profil maka isi dengan foto profil standar
                                if (user.image.isEmpty()) user.image =
                                    "https://tanjungpinangkota.bawaslu.go.id/wp-content/uploads/2020/05/default-1.jpg"
                                // masukkan foto profil menggunakan glide, circleCrop agar jadi lingkaran
                                Glide.with(this@ProfileFragment).load(user.image)
                                    .circleCrop()
                                    .into(profilUser)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }
            )
        }
    }

}