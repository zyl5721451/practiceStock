package com.practicestock.app.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.practicestock.app.R
import com.practicestock.app.data.DataManager
import com.practicestock.app.databinding.FragmentOpenPositionBinding
import com.practicestock.app.model.OpenReason
import com.practicestock.app.model.TradeRecord
import com.practicestock.app.model.TradeResult
import com.practicestock.app.utils.ImageUtils
import java.io.File
import java.util.*

class OpenPositionFragment : Fragment() {
    
    private var _binding: FragmentOpenPositionBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var dataManager: DataManager
    private var selectedImageUri: Uri? = null
    private var tempImageFile: File? = null
    
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                displayImage(uri)
            }
        }
    }
    
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            tempImageFile?.let { file ->
                selectedImageUri = Uri.fromFile(file)
                displayImage(selectedImageUri!!)
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenPositionBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dataManager = DataManager.getInstance(requireContext())
        
        setupSpinners()
        setupImageUpload()
        setupButtons()
    }
    
    private fun setupSpinners() {
        // 开仓理由下拉选择
        val reasonOptions = OpenReason.values().map { it.displayName }
        val reasonAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, reasonOptions)
        binding.reasonSpinner.setAdapter(reasonAdapter)
        binding.reasonSpinner.setText(reasonOptions[0], false)
        
        // 结果下拉选择
        val resultOptions = TradeResult.values().map { it.displayName }
        val resultAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, resultOptions)
        binding.resultSpinner.setAdapter(resultAdapter)
        binding.resultSpinner.setText(resultOptions[0], false)
    }
    
    private fun setupImageUpload() {
        binding.imageCard.setOnClickListener {
            showImagePickerDialog()
        }
    }
    
    private fun setupButtons() {
        binding.saveButton.setOnClickListener {
            saveRecord()
        }
        
        binding.clearButton.setOnClickListener {
            clearForm()
        }
    }
    
    private fun showImagePickerDialog() {
        val options = arrayOf(
            getString(R.string.take_photo),
            getString(R.string.choose_from_gallery)
        )
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_image))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkPermissionsAndTakePhoto()
                    1 -> checkPermissionsAndPickFromGallery()
                }
            }
            .show()
    }
    
    private fun checkPermissionsAndTakePhoto() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        takePhoto()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                    }
                }
                
                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }
    
    private fun checkPermissionsAndPickFromGallery() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                override fun onPermissionGranted(response: com.karumi.dexter.listener.single.PermissionGrantedResponse) {
                    pickFromGallery()
                }
                
                override fun onPermissionDenied(response: com.karumi.dexter.listener.single.PermissionDeniedResponse) {
                    Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
                
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }
    
    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        tempImageFile = ImageUtils.createImageFile(requireContext())
        tempImageFile?.let { file ->
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        }
    }
    
    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }
    
    private fun displayImage(uri: Uri) {
        binding.uploadPlaceholder.visibility = View.GONE
        binding.imagePreview.visibility = View.VISIBLE
        
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.imagePreview)
    }
    
    private fun saveRecord() {
        val reasonText = binding.reasonSpinner.text.toString()
        val resultText = binding.resultSpinner.text.toString()
        
        val reason = OpenReason.fromDisplayName(reasonText)
        val result = TradeResult.fromDisplayName(resultText)
        
        if (reason == null || result == null) {
            Toast.makeText(requireContext(), getString(R.string.save_failed), Toast.LENGTH_SHORT).show()
            return
        }
        
        val imagePath = selectedImageUri?.let { uri ->
            ImageUtils.saveImageToInternalStorage(requireContext(), uri)
        }
        
        val record = TradeRecord(
            id = dataManager.generateId(),
            openReason = reason,
            result = result,
            imagePath = imagePath,
            createdTime = Date()
        )
        
        dataManager.saveRecord(record)
        Toast.makeText(requireContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show()
        
        clearForm()
    }
    
    private fun clearForm() {
        // 重置下拉选择到第一个选项
        val reasonOptions = OpenReason.values().map { it.displayName }
        binding.reasonSpinner.setText(reasonOptions[0], false)
        
        val resultOptions = TradeResult.values().map { it.displayName }
        binding.resultSpinner.setText(resultOptions[0], false)
        
        // 清空图片预览
        selectedImageUri = null
        binding.imagePreview.visibility = View.GONE
        binding.uploadPlaceholder.visibility = View.VISIBLE
        binding.imagePreview.setImageDrawable(null)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}