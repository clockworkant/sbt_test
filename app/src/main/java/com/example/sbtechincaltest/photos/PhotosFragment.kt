package com.example.sbtechincaltest.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sbtechincaltest.R
import com.example.sbtechincaltest.databinding.FragmentPhotosBinding
import com.example.sbtechincaltest.databinding.ItemPhotoBinding
import com.example.sbtechincaltest.photos.data.PhotoData
import com.squareup.picasso.Picasso
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

        viewmodel.photos.observe(viewLifecycleOwner, {
            photoAdapter.submitList(it)
        })

        viewmodel.loadPhotos()
    }

    private fun initRecyclerView() {
        binding.photosRv.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
            layoutManager = linearLayoutManager
            adapter = photoAdapter
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
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_baseline_arrow_back_ios_24)
                .into(binding.photoImage);
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
        return oldItem.title == newItem.title //TODO improve
    }
}

