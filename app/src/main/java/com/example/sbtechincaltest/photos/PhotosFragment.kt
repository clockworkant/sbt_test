package com.example.sbtechincaltest.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.sbtechincaltest.R
import com.example.sbtechincaltest.databinding.FragmentPhotosBinding
import com.example.sbtechincaltest.databinding.ItemPhotoBinding
import com.example.sbtechincaltest.photos.data.PhotoData
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotosFragment : Fragment() {

    private val viewmodel: PhotosViewModel by viewModel()

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private val photoAdapter = PhotoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView()
        initSwipeToRefresh()

        viewmodel.photosModel.observe(viewLifecycleOwner, {
            processModel(it)
        })

        viewmodel.loadPhotos()
    }

    private fun initSwipeToRefresh() {
        binding.photosListRefreshlayout.setOnRefreshListener { viewmodel.loadPhotos() }
    }

    private fun processModel(photosModel: PhotosModel) {
        showProgress(photosModel.showLoading)
        photosModel.photos?.let { photoAdapter.submitList(it) }
        photosModel.errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    private fun showProgress(showLoading: Boolean) {
        binding.photosListRefreshlayout.isRefreshing = showLoading
    }

    private fun initRecyclerView() {
        binding.photosRv.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
            layoutManager = linearLayoutManager
            adapter = photoAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    linearLayoutManager.orientation
                )
            )
        }
    }

    private fun initToolbar() {
        binding.toolbar.navigationIcon = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_arrow_back_ios_24,
            activity?.theme
        )
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }
}

private class PhotoAdapter :
    ListAdapter<PhotoData, PhotoAdapter.PhotoViewHolder>(PhotoDiffCallback) {

    class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoData) {
            Picasso.get()
                .load(photo.thumbnailUrl)
                .transform(RoundedCornersTransformation(10, 0))
                .placeholder(R.drawable.ic_launcher_background)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.photoImage)
            binding.photoTitle.text = photo.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

object PhotoDiffCallback : DiffUtil.ItemCallback<PhotoData>() {
    override fun areItemsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
        return (oldItem.title == newItem.title) && (oldItem.thumbnailUrl == newItem.thumbnailUrl)
    }
}

