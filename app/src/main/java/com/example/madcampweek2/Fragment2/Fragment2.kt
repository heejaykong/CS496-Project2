package com.example.madcampweek2.Fragment2

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.fragment_2.*
import kotlinx.android.synthetic.main.fragment_2.view.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class Fragment2 : Fragment() {
    //private val Gallery = 0
    //private var imageList = emptyArray<ImageData>()

    private lateinit var mCamera : Camera
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ImageDataset = getFileList(requireContext(), MediaStoreFileType.IMAGE)
        var mutableImageDataset = ImageDataset.toMutableList()
        val folderDataset = mutableListOf<ImageData>()
        val countImages = mutableListOf<Int>()
        val folderId = mutableListOf<Long>()
        print(ImageDataset.size)
        val recyclerview: RecyclerView = view.gallery
        recyclerview.addItemDecoration(GridItemDecoration(10))
        recyclerview.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerview.adapter = GalleryAdapter(requireContext(), mutableImageDataset, countImages)

        fab.setOnClickListener{
            takePicture()
        }
    }

    private val REQUEST_TAKE_PHOTO = 1
    private val REQ_CAMERA_PERMISSION = 100
    private fun takePicture(){
        var permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        if(permission == PackageManager.PERMISSION_DENIED){
            //권한 없어서 요청
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQ_CAMERA_PERMISSION)
        }else{
            //권한 있음
            var capture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity?.startActivityForResult(capture, REQUEST_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if(resultCode == RESULT_OK) {
                var bundle: Bundle? = data?.getExtras()
                var bitmap: Bitmap = bundle?.get("data") as Bitmap
                var changedUri: Uri = BitmapToUri(this.requireContext(), bitmap)
            }
        }
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }


    fun BitmapToUri(context: Context, bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    private lateinit var currentPhotoPath: String
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply{
            currentPhotoPath = absolutePath
            Log.d("test", "currentPhotoPath : $currentPhotoPath")
        }
    }

    // 갤러리에 파일을 추가하는 함수.
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            Log.d("test", "currentPhotoPath2 : $currentPhotoPath")
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            getActivity()?.sendBroadcast(mediaScanIntent)
        }
    }

    private fun setPic(){
        val ImageDataset = getFileList(requireContext(), MediaStoreFileType.IMAGE)
        var newImageDataset = ImageDataset.toMutableList()
        val MDF = ImageData(Uri.parse(currentPhotoPath))
        newImageDataset.add(MDF)

        val recyclerview: RecyclerView = requireView().gallery
        val mAdapter = recyclerview.adapter
        mAdapter?.notifyItemInserted(0)
    }

    private fun getFileList(context: Context, type: MediaStoreFileType): List<ImageData> {
        val imageList = mutableListOf<ImageData>()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_TAKEN,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Files.FileColumns.BUCKET_ID
        )
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_TAKEN} DESC"
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dateTakenColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val bucketIDColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = Date(cursor.getLong(dateTakenColumn))
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = Uri.withAppendedPath(
                    type.externalContentUri,
                    id.toString()
                )
                val bucketID = cursor.getLong(bucketIDColumn)
                val bucketName = cursor.getString(bucketNameColumn)
                Log.d(
                    "test",
                    "id: $id, display_name: $displayName, date_taken: $dateTaken, content_uri: $contentUri\n"
                )
                val MDF = ImageData(contentUri)
                imageList.add(MDF)
            }
        }
        return imageList
    }

    private fun <E> listContainsContitionedItem(
        list: MutableList<E>,
        item: E,
        condition: (E, E) -> Boolean
    ): Boolean {
        list.forEach {
            when (condition(it, item)) {
                true -> return true
            }
        }
        return false
    }

    enum class MediaStoreFileType(
        val externalContentUri: Uri,
        val mimeType: String,
        val pathByDCIM: String
    ) {
        IMAGE(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*", "/image"),
        //AUDIO(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio/*", "/audio"),
        //VIDEO(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*", "/video");
    }



}