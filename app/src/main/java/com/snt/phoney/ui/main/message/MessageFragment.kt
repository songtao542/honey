package com.snt.phoney.ui.main.message

import jiguang.chat.fragment.ConversationListFragment

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class MessageFragment : ConversationListFragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_message_list, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        enableOptionsMenu(messageToolbar, false)
//        with(messageRecyclerView) {
//            layoutManager = LinearLayoutManager(context)
//            adapter = MessageRecyclerViewAdapter()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater?.inflate(R.menu.message, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item?.itemId) {
//            R.id.friendRecommend -> {
//
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//    }


    companion object {
        @JvmStatic
        fun newInstance() = MessageFragment()
    }
}
